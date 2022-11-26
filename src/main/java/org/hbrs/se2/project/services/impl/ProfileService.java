package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.services.ProfileServiceInterface;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ProfileService implements ProfileServiceInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentHasMajorRepository studentHasMajorRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private StudentHasSkillRepository studentHasSkillRepository;
    @Autowired
    private StudentHasTopicRepository studentHasTopicRepository;
    @Autowired
    private EntityCreationService entityCreationService;

    @Override
    public void saveStudentData(UserDTO user, StudentDTO student, List<String> major, List<String> topic, List<String> skill) {
        userRepository.save(entityCreationService.userFactory().createEntity(user));
        studentRepository.save(entityCreationService.studentFactory().createEntity(student));
        major.parallelStream().forEach(m -> updateStudyMajor(m, student.getStudentid()));
        topic.parallelStream().forEach(t -> updateTopics(t, student.getStudentid()));
        skill.parallelStream().forEach(s -> updateSkills(s, student.getStudentid()));
    }

    private void updateStudyMajor(String major, int studentid) {
        // get major data from database by major attribute
        MajorDTO majorDTO = majorRepository.findByMajor(major);
        // check if found major is null
        if (majorDTO == null) {
            // if major does not exist, create new major from input
            majorRepository.save(entityCreationService.majorFactory().createEntity(major));
            // get saved major data
            majorDTO = majorRepository.findByMajor(major);
        }
        // create new studentHasMajor entity
        studentHasMajorRepository.save(entityCreationService.shmFactory()
                .createEntity(new int[]{studentid, majorDTO.getMajorid()}));
    }

    private void updateTopics(String topic, int studentid) {
        // get topic data from database by major attribute
        TopicDTO topicDTO = topicRepository.findByTopic(topic);
        // check if found topic is null
        if (topicDTO == null) {
            // if topic does not exist, create new topic from input
            topicRepository.save(entityCreationService.topicFactory().createEntity(topic));
            // get topic major data
            topicDTO = topicRepository.findByTopic(topic);
        }
        // create new studentHasMajor entity
        studentHasTopicRepository.save(entityCreationService.shtFactory()
                .createEntity(new int[]{studentid, topicDTO.getTopicid()}));
    }

    private void updateSkills(String skill, int studentid) {
        // same process as in updateMajors and updateTopics
        SkillDTO skillDTO = skillRepository.findBySkill(skill);
        if (skillDTO == null) {
            skillRepository.save(entityCreationService.skillFactory().createEntity(skill));
            skillDTO = skillRepository.findBySkill(skill);
        }
        studentHasSkillRepository.save(entityCreationService.shsFactory()
                .createEntity(new int[]{studentid, skillDTO.getSkillid()}));
    }

    @Override
    public StudentDTO getStudentProfile(int userid) {
        return studentRepository.findStudentByUserid(userid);
    }

    @Override
    public List<MajorDTO> getMajorOfStudent(int userid) {
        // comment: maybe we should return a list of major dtos and not strings
        // important for data binding in view
        // list for majors
        List<MajorDTO> majors = new ArrayList<>();
        // get student with matching user id
        // get data from student_has_major table with matching student ids
        // get matching majors from major table with major id from studentHasMajor list
        for (StudentHasMajorDTO studentHasMajor : studentHasMajorRepository
                .findByStudentid(studentRepository
                        .findStudentByUserid(userid)
                        .getStudentid())) {
            majors.add(majorRepository.findByMajorid(studentHasMajor.getMajorid()));
        }
        // return list of majors
        return majors;
    }

    @Override
    public List<TopicDTO> getTopicOfStudent(int userid) {
        // same process as in getMajorOfStudent method
        List<TopicDTO> topics = new ArrayList<>();
        for (StudentHasTopicDTO studentHasTopic : studentHasTopicRepository
                .findByStudentid(studentRepository
                        .findStudentByUserid(userid)
                        .getStudentid())) {
            topics.add(topicRepository.findByTopicid(studentHasTopic.getTopicid()));
        }
        return topics;
    }

    @Override
    public List<SkillDTO> getSkillOfStudent(int userid) {
        // same process as in getMajorOfStudent method
        List<SkillDTO> skills = new ArrayList<>();
        for (StudentHasSkillDTO studentHasSkill : studentHasSkillRepository
                .findByStudentid(studentRepository
                        .findStudentByUserid(userid)
                        .getStudentid())) {
            skills.add(skillRepository.findBySkillid(studentHasSkill.getSkillid()));
        }
        return skills;
    }

    @Override
    public void removeMajor(int userid, int majorid) {
        studentHasMajorRepository.deleteByStudentidAndMajorid(studentRepository.findStudentByUserid(userid).getStudentid(), majorid);
    }

    @Override
    public void removeTopic(int userid, int topicid) {
        studentHasTopicRepository.deleteByStudentidAndTopicid(studentRepository.findStudentByUserid(userid).getStudentid(), topicid);
    }

    @Override
    public void removeSkill(int userid, int skillid) {
        studentHasSkillRepository.deleteByStudentidAndSkillid(studentRepository.findStudentByUserid(userid).getStudentid(), skillid);
    }

    public Set<StudentDTO> getStudentsMatchingKeyword(String keyword) {
        // get all data for filtering
        Set<StudentDTO> matchingStudents = new HashSet<>();

        List<StudentDTO> temp = studentRepository.getAll();
        temp.parallelStream().forEach(studentDTO -> {
            // String array for saving all data
            List<String> list = new ArrayList<>();

            // get skills, majors, topics of one student
            List<SkillDTO> skills = getSkillOfStudent(studentDTO.getUserid());
            List<TopicDTO> topics = getTopicOfStudent(studentDTO.getUserid());
            List<MajorDTO> majors = getMajorOfStudent(studentDTO.getUserid());

            // save university into string array
            if(studentDTO.getUniversity() != null) {
                list.add(studentDTO.getUniversity());
            }
            skills.parallelStream().forEach(skill -> list.add(skill.getSkill()));
            topics.parallelStream().forEach(topic -> list.add(topic.getTopic()));
            majors.parallelStream().forEach(major -> list.add(major.getMajor()));

            list.parallelStream().forEach(element -> {
                if(element.equalsIgnoreCase(keyword)){
                    matchingStudents.add(studentDTO);
                }
            });
        });
        return matchingStudents;
    }

    public UserDTO getUserByUserid(int userid) {
        return userRepository.findUserByUserid(userid);
    }
}


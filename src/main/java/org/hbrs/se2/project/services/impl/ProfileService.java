package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.services.ProfileServiceInterface;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    private ModelMapper mapper = new ModelMapper();


    @Override
    public void saveStudentData(int id, UserDTO user, StudentDTO student, String university, List<String> major, List<String> topic, List<String> skill) throws DatabaseUserException {
        userRepository.save(entityCreationService.userFactory().createEntity(user));
        studentRepository.save(entityCreationService.studentFactory().createEntity(student));
        major.parallelStream().forEach(m -> {
            try {
                updateStudyMajor(m, id);
            } catch (DatabaseUserException e) {
                throw new RuntimeException(e);
            }
        });
        topic.parallelStream().forEach(t -> {
            try {
                updateTopics(t, id);
            } catch (DatabaseUserException e) {
                throw new RuntimeException(e);
            }
        });
        skill.parallelStream().forEach(s -> {
            try {
                updateSkills(s, id);
            } catch (DatabaseUserException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateStudyMajor(String major, int userid) throws DatabaseUserException {
        // get student data through user id
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        // check if student with the user id exists
        if (studentDTO == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
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
                .createEntity(new int[]{studentDTO.getStudentid(), majorDTO.getMajorid()}));
    }

    private void updateTopics(String topic, int userid) throws DatabaseUserException {
        // get student data through user id
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        // check if student with the user id exists
        if (studentDTO == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
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
                .createEntity(new int[]{studentDTO.getStudentid(), topicDTO.getTopicid()}));
    }

    private void updateSkills(String skill, int userid) throws DatabaseUserException {
        // same process as in updateMajors and updateTopics
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if (studentDTO == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
        SkillDTO skillDTO = skillRepository.findBySkill(skill);
        if (skillDTO == null) {
            skillRepository.save(entityCreationService.skillFactory().createEntity(skill));
            skillDTO = skillRepository.findBySkill(skill);
        }
        studentHasSkillRepository.save(entityCreationService.shsFactory()
                .createEntity(new int[]{studentDTO.getStudentid(), skillDTO.getSkillid()}));
    }

    private void updateUniversity(String university, int userid) throws DatabaseUserException {
        // get student with matching user id
        StudentDTOImpl student = mapper.map(studentRepository.findStudentByUserid(userid), StudentDTOImpl.class);
        // check if student is null
        if (student == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
        student.setUniversity(university);
        // update student entity with university attribute
        studentRepository.save(entityCreationService.studentFactory().createEntity(student));
    }

    public StudentDTO getStudentProfile(int userid) {
        return studentRepository.findStudentByUserid(userid);
    }

    @Override
    public String getUniversityOfStudent(int userid) {
        // get student with matching user id
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        // if university is null we to return an empty string
        // because the grid needs a value but not null
        if (studentDTO.getUniversity() == null) {
            return "";
        }
        return studentDTO.getUniversity();
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

}

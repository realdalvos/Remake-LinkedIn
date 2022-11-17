package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.entities.*;
import org.hbrs.se2.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileControl {
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

    public void saveStudentData(int id, String major, String university, String topic, String skill) throws DatabaseUserException {
        // if input is not null or not empty, save student data
        if (major != null && !major.equals("")) {
            updateStudyMajor(major, id);
        }
        if (university != null && !university.equals("")) {
            updateUniversity(university, id);
        }
        if (topic != null && !topic.equals("")) {
            updateTopics(topic, id);
        }
        if (skill != null && !skill.equals("")) {
            updateSkills(skill, id);
        }
    }

    public void updateStudyMajor(String major, int userid) throws DatabaseUserException {
        // get student data through user id
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        // check if student with the user id exists
        if(studentDTO == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
        // get major data from database by major attribute
        MajorDTO majorDTO = majorRepository.findByMajor(major);
        // check if found major is null
        if(majorDTO == null){
            // if major does not exist, create new major from input
            Major majorEntity = new Major();
            majorEntity.setMajor(major);
            majorRepository.save(majorEntity);
            // get saved major data
            majorDTO = majorRepository.findByMajor(major);
        }
        // create new studentHasMajor entity
        StudentHasMajor studentHasMajor = new StudentHasMajor();
        studentHasMajor.setMajorid(majorDTO.getMajorid());
        studentHasMajor.setStudentid(studentDTO.getStudentid());
        studentHasMajorRepository.save(studentHasMajor);
    }

    // methods are never called, see comments in profile View
    public void updateTopics(String topic, int userid) throws DatabaseUserException {
        // get student data through user id
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        // check if student with the user id exists
        if(studentDTO == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
        // get topic data from database by major attribute
        TopicDTO topicDTO = topicRepository.findByTopic(topic);
        // check if found topic is null
        if(topicDTO == null){
            // if topic does not exist, create new topic from input
            Topic topicEntity = new Topic();
            topicEntity.setTopic(topic);
            topicRepository.save(topicEntity);
            // get topic major data
            topicDTO = topicRepository.findByTopic(topic);
        }
        // create new studentHasMajor entity
        StudentHasTopic studentHasTopic = new StudentHasTopic();
        studentHasTopic.setTopicid((topicDTO.getTopicid()));
        studentHasTopic.setStudentid(studentDTO.getStudentid());
        studentHasTopicRepository.save(studentHasTopic);
    }

    // methods are never called, see comments in profile Views
    public void updateSkills(String skill, int userid) throws DatabaseUserException {
        // same process as in updateMajors and updateTopics
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if(studentDTO == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
        SkillDTO skillDTO = skillRepository.findBySkill(skill);
        if(skillDTO == null){
            Skill skillEntity = new Skill();
            skillEntity.setSkill(skill);
            skillRepository.save(skillEntity);
            skillDTO = skillRepository.findBySkill(skill);
        }
        StudentHasSkill studentHasSkill = new StudentHasSkill();
        studentHasSkill.setSkillid(skillDTO.getSkillid());
        studentHasSkill.setStudentid(studentDTO.getStudentid());
        studentHasSkillRepository.save(studentHasSkill);
    }

    public void updateUniversity(String university, int userid) throws DatabaseUserException {
        // get student with matching user id
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        // check if student is null
        if(studentDTO == null) {
            throw new DatabaseUserException("There is no Student with the passed id");
        }
        // create new student entity to update university attribute
        Student student = new Student();
        student.setUserid(studentDTO.getUserid());
        student.setStudentid(studentDTO.getStudentid());
        student.setFirstname(studentDTO.getFirstname());
        student.setLastname(studentDTO.getLastname());
        student.setUniversity(studentDTO.getUniversity());
        student.setMatrikelnumber(studentDTO.getMatrikelnumber());
        student.setUniversity(university);
        // save updated student
        studentRepository.save(student);
    }

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

    public List<String> getMajorOfStudent(int userid) {
        // major dto
        MajorDTO majorDTO;
        // comment: maybe we should return a list of major dtos and not strings
        // important for data binding in view
        // list for majors
        List<String> majors = new ArrayList<>();
        // get student with matching user id
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        // get data from student_has_major table with matching student ids
        List<StudentHasMajorDTO> studentHasMajors =
                studentHasMajorRepository.findByStudentid(studentDTO.getStudentid());
        // get matching majors from major table with major id from studentHasMajor list
        for (StudentHasMajorDTO studentHasMajor : studentHasMajors) {
            majorDTO = majorRepository.findByMajorid(studentHasMajor.getMajorid());
            // check if major is null
            // if major is null, we return an empty string like getUniversityOfStudent method
            if(majorDTO.getMajor() == null) {
                majors.add("");
            } else {
                majors.add(majorDTO.getMajor());
            }
        }
        // return list of majors
        return majors;
    }
    public List<String> getTopicOfStudent(int userid){
        // same process as in getMajorOfStudent method
        TopicDTO topicDTO;
        List<String> topics = new ArrayList<>();
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        List<StudentHasTopicDTO> studentHasTopics = studentHasTopicRepository.findByStudentid(studentDTO.getStudentid());
        for(StudentHasTopicDTO studentHasTopic : studentHasTopics){
            topicDTO = topicRepository.findByTopicid(studentHasTopic.getTopicid());
            // check if topic is null
            if(topicDTO.getTopic() == null) {
                topics.add("");
            } else {
                topics.add(topicDTO.getTopic());
            }
        }
        return topics;
    }

    public List<String> getSkillOfStudent(int userid){
        // same process as in getMajorOfStudent method
        SkillDTO skillDTO;
        List<String> skills = new ArrayList<>();
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        List<StudentHasSkillDTO> studentHasSkills = studentHasSkillRepository.findByStudentid(studentDTO.getStudentid());
        for(StudentHasSkillDTO studentHasSkill : studentHasSkills){
            skillDTO = skillRepository.findBySkillid(studentHasSkill.getSkillid());
            // check if skill is null
            if(skillDTO.getSkill() == null) {
                skills.add("");
            } else {
                skills.add(skillDTO.getSkill());
            }
        }
        return skills;
    }
}




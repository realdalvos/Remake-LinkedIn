package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.entities.*;
import org.hbrs.se2.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileControl {
    // we might remove this if it will not be used
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

    // need this but has to be used in a method, see comments in following methods
    @Autowired
    private StudentHasSkillRepository studentHasSkillRepository;

    @Autowired
    private StudentHasTopicRepository studentHasTopicRepository;

    public void updateStudyMajor(String major, int userid){
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if(studentDTO == null) {
            System.out.println("Is null");
            // we might throw an exception here instead of a print-out statement, see warning line 49
        }
       MajorDTO majorDTO = majorRepository.findByMajor(major);
        if(majorDTO == null){
            Major majorEntity = new Major();
            majorEntity.setMajor(major);
            majorRepository.save(majorEntity);
            majorDTO = majorRepository.findByMajor(major);
        }
        StudentHasMajor studentHasMajor = new StudentHasMajor();
        studentHasMajor.setMajorid(majorDTO.getMajorid());
        studentHasMajor.setStudentid(studentDTO.getStudentid());
        studentHasMajorRepository.save(studentHasMajor);
    }

    // methods are never called, see comments in profile View
    public void updateTopics(String topic, int userid){
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if(studentDTO == null) {
            System.out.println("Is null");
            // we might throw an exception here instead of a print-out statement, see warning line 68
        }
        TopicDTO topicDTO = topicRepository.findByTopic(topic);
        if(topicDTO == null){
            Topic topicEntity = new Topic();
            topicEntity.setTopic(topic);
            topicRepository.save(topicEntity);
            topicDTO = topicRepository.findByTopic(topic);
        }
        StudentHasTopic studentHasTopic = new StudentHasTopic();
        studentHasTopic.setTopicid((topicDTO.getTopicid()));
        studentHasTopic.setStudentid(studentDTO.getStudentid());
        studentHasTopicRepository.save(studentHasTopic);
    }

    // methods are never called, see comments in profile Views
    public void updateSkills(String skill, int userid){
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if(studentDTO == null) {
            System.out.println("Is null");
            // we might throw an exception here instead of a print-out statement
        }
        SkillDTO skillDTO = skillRepository.findBySkill(skill);
        if(skillDTO == null){
            Skill skillEntity = new Skill();
            skillEntity.setSkill(skill);
            skillRepository.save(skillEntity);
            skillDTO = skillRepository.findBySkill(skill);
        }
        // there are steps missing see methods updateTopics or updateStudyMajor
        // after saving a skill when it does not exist, we have to create the
        // studentHasSkill table with the skillid from skillDTO and the studentid from studentDTO
        // and save it in the studentHasSkill database table
    }

    public void updateUniversity(String university, int userid){
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if(studentDTO == null) {
            System.out.println("Is null");
        }
        Student student = new Student();
        student.setUserid(studentDTO.getUserid());
        student.setStudentid(studentDTO.getStudentid());
        student.setFirstname(studentDTO.getFirstname());
        student.setLastname(studentDTO.getLastname());
        student.setUniversity(studentDTO.getUniversity());
        student.setMatrikelnumber(studentDTO.getMatrikelnumber());
        //student.setStudyMajor(studentDTO.getStudyMajor()); // line can be removed
        student.setUniversity(university);
        studentRepository.save(student);
    }

    // if statement has empty body
    public String getUniversityOfStudent(int userid) {
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if ( studentDTO.getUniversity() == null ){
        }
        return studentDTO.getUniversity();
    }

    // is commented out because studentDTO does NOT have attribute major
    // might also remove this
    // usage is not clear
/*
    public String getMajorOfStudent(int userid) {
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if ( studentDTO.getMajor() == null ){
            return "";
        }
        return studentDTO.getMajor();
    }
 */
}


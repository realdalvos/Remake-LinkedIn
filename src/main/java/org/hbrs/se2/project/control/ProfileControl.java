package org.hbrs.se2.project.control;

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
        StudentHasSkill studentHasSkill = new StudentHasSkill();
        studentHasSkill.setSkillid(skillDTO.getSkillid());
        studentHasSkill.setStudentid(studentDTO.getStudentid());
        studentHasSkillRepository.save(studentHasSkill);
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

    public String getUniversityOfStudent(int userid) {
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if ( studentDTO.getUniversity() == null ){
            return "";
        }
        return studentDTO.getUniversity();
    }

    public List<String> getMajorOfStudent(int userid) {
        // temp major
        MajorDTO majorDTO;
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
            if(majorDTO.getMajor() == null) {
                majors.add("");
            } else {
                majors.add(majorDTO.getMajor());
            }
        }
        // return list of majors
        return majors;
    }
/*
    public List<MajorDTO>   getMajorOfStudentAsDTOLIST(int userid) {
        // temp major
        MajorDTO majorDTO;

        List<MajorDTO> majorDTOList = new ArrayList<>();
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
            if(majorDTO.getMajor() == null) {
                majors.add("");
            } else {
                majors.add(majorDTO.getMajor());
            }
            majorDTOList = majorRepository.findByMajoridGiveList(studentHasMajor.getMajorid());
            System.out.println(majorDTOList);
        }
        // return list of majors
        //return majorDTOList;
    }

    */





    public List<String> getTopicOfStudent(int userid){
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




package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.entities.*;
import org.hbrs.se2.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileControl {
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

    public void updateStudyMajor(String major, int userid){
        StudentDTO studentDTO = studentRepository.findStudentByUserid(userid);
        if(studentDTO == null) {
            System.out.println("Is null");
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
       // student.setStudyMajor(studentDTO.getStudyMajor());
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
}

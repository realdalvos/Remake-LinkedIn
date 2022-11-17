package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Student;

public class StudentFactory {

    /**
     * @param userDTO DTO of user
     * @param studentDTO DTO of student
     * @return Newly created Student-Entity*/
    public static Student createStudent(StudentDTO studentDTO, UserDTO userDTO) {

        Student student = new Student();
        // pass parameters from studentDTO to student
        student.setFirstname(studentDTO.getFirstname());
        student.setLastname(studentDTO.getLastname());
        student.setMatrikelnumber(studentDTO.getMatrikelnumber());
        student.setUserid(userDTO.getUserid());
        student.setStudentid(studentDTO.getStudentid());
        student.setUniversity(studentDTO.getUniversity());

        return student;

    }
}

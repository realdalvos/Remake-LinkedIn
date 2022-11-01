package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Student;

public class StudentFactory {

    public static Student createStudent(StudentDTO studentDTO, UserDTO userDTO) {

        Student student = new Student();

        student.setFirstname(studentDTO.getFirstname());
        student.setLastname(studentDTO.getLastname());
        student.setMatrikelnumber(studentDTO.getMatrikelnumber());
        student.setUserid(userDTO.getUserid());

        return student;

    }
}

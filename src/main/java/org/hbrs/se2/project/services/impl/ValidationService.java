package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    public boolean checkUsernameUnique(String username) {
        // check if username or email already exists
        UserDTO userDTO = userRepository.findUserByUsername(username);

        if (userDTO != null) {
            return false;
        }
        return true;
    }

    public boolean checkEmailUnique(String email) {
        // check if email already exists
        UserDTO userDTO = userRepository.findUserByEmail(email);

        if (userDTO != null) {
            return false;
        }
        return true;
    }

    public boolean checkMatrikelnumberUnique(String matrikelnumber) {
        // check if matrikelnumber already exists
        StudentDTO studentDTO = studentRepository.findStudentByMatrikelnumber(matrikelnumber);

        if (studentDTO != null) {
            return false;
        }
        return true;
    }

}

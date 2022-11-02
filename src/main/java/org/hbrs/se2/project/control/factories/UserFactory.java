package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFactory {

    public static User createUser (UserDTO userDTO) {

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        //Hashing password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return user;

    }
}

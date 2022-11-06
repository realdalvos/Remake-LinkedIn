package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFactory {

    /**
     * Use this Method only if the password is already hashed, otherwise use method createUserWithoutHashedPassword.
     * This could be the case when updating a user.
     * @param userDTO DTO of user
     * @return Created User Entity*/
    public static User createUser (UserDTO userDTO) {
        return helper(userDTO, userDTO.getPassword());
    }

    /**
     * Use this Method only if the password is NOT already hashed, otherwise use method createUser.
     * This could be the case when updating a user.
     * @param userDTO DTO of user
     * @return Created User Entity*/
    public static User createUserWithoutHashedPassword(UserDTO userDTO) {
        //Hashing password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        return helper(userDTO, hashedPassword);
    }

    private static User helper(UserDTO userDTO, String password){
        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        user.setUserid(userDTO.getUserid());
        user.setPassword(password);

        return user;
    }
}

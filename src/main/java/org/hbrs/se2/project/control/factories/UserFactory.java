package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.User;

public class UserFactory {

    /**
     * Creates the user Entity "copy" for a given userDTO.
     * @param userDTO DTO of user
     * @return Created User Entity*/
    public static User createUser (UserDTO userDTO) {
        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        user.setUserid(userDTO.getUserid());
        user.setPassword(userDTO.getPassword());

        return user;
    }
}

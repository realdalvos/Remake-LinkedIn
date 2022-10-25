package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.UserDTO;

import java.util.Objects;

public class AuthorizationControl {

    /**
     * Method for checking the role of a user
     */
    public boolean hasUserRole(UserDTO user, String role) {
        // trim because user role does somehow have white spaces
        return user.getRole().trim().equals(role);
    }
}

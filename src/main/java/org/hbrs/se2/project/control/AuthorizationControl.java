package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.UserDTO;

public class AuthorizationControl {

    /**
     * Method for checking the role of a user
     * @param user UserDTO to check
     * @param role desired role
     */
    public boolean hasUserRole(UserDTO user, String role) {
        // trim because user role does somehow have white spaces
        return user.getRole().trim().equals(role);
    }
}

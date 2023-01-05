package org.hbrs.se2.project.control;

import com.vaadin.flow.component.UI;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.services.impl.LoginService;
import org.springframework.stereotype.Controller;

@Controller
public class AuthorizationControl {

    final LoginService loginService;

    public AuthorizationControl(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Authenticate the user that wants to login in with his credentials.
     * @param username the username of the user
     * @param password the password of the user
     * @return true if authentication was successfull, false if not.
     * @throws DatabaseUserException Something went wrong with the Database
     * */
    public boolean authenticate(String username, String password) throws DatabaseUserException {
        return loginService.authenticate(username, password);
    }

    /**
     * Get current authenticated user
     * @return last successfully authenticated user as UserDTO
     * */
    public UserDTO getCurrentUser() {
        return loginService.getCurrentUser();
    }

    /**
     * Method for checking the role of a user
     * @param user UserDTO to check
     * @param role desired role
     */
    public boolean hasUserRole(UserDTO user, String role) {
        // trim because user role does somehow have white spaces
        return user.getRole().trim().equals(role);
    }

    /**
     * Logout the current user by closing his session and redirecting him to the default page (login page)
     */
    public void logoutUser() {
        UI.getCurrent().getSession().close();
        UI.getCurrent().getPage().setLocation("/");
    }

    public boolean isBannedCompany(UserDTO user) {
        return loginService.isBannedCompany(user);
    }
}

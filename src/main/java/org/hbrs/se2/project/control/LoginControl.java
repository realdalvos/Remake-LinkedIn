package org.hbrs.se2.project.control;

import org.hbrs.se2.project.services.impl.LoginService;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.springframework.stereotype.Controller;

@Controller
public class LoginControl {
    final LoginService loginService;

    public LoginControl(LoginService loginService) {
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


}


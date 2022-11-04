package org.hbrs.se2.project.views.studentViews.services;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;

public interface LoginServiceInterface {

    /**
     * Autheticate the user that wants to login in with his cedentials.
     * @param username the username of the user
     * @param password the password of the user
     * @return true if authentication was successfull, false if not.
     * @throws DatabaseUserException Something went wrong with the Database
     * */
    public boolean authenticate(String username, String password) throws DatabaseUserException;

    /**
     * @return last successfully authenticated user as UserDTO
     * */
    public UserDTO getCurrentUser();
}

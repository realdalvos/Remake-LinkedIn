package org.hbrs.se2.project.views.studentViews.services;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;

public interface LoginServiceInterface {
    public boolean authenticate(String username, String password) throws DatabaseUserException;

    public UserDTO getCurrentUser();
}

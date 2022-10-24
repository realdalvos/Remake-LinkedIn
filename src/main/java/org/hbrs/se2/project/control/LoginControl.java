package org.hbrs.se2.project.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.UserRepository;

@Component
public class LoginControl {

    @Autowired
    private UserRepository repository;

    private UserDTO userDTO = null;

    public boolean authenticate(String username, String password) throws DatabaseUserException {
        UserDTO tmpUser = this.getUserWithJPA(username, password);

        if(tmpUser == null) {
            return false;
        }
        this.userDTO = tmpUser;
        return true;
    }

    public UserDTO getCurrentUser() {
        return this.userDTO;
    }

    private UserDTO getUserWithJPA(String username, String password) throws DatabaseUserException {
        UserDTO userTmp;
        try {
            userTmp = repository.findUserByUsernameAndPassword(username, password);
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occured while trying to connect to database with JPA");
        }
        return userTmp;
    }
}


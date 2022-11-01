package org.hbrs.se2.project.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.UserRepository;

@Component
public class LoginControl {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDTO userDTO = null;

    /**
     * Autheticate the user that wants to login in with his cedentials.
     * @param username the username of the user
     * @param password the password of the user
     * @return true if authentication was successfull, false if not.
     * @throws DatabaseUserException
     * */
    public boolean authenticate(String username, String password) throws DatabaseUserException {
        UserDTO tmpUser = this.getUserWithJPA(username);

        if(tmpUser == null || !passwordEncoder.matches(password, tmpUser.getPassword().trim())) {
            return false;
        }
        this.userDTO = tmpUser;
        return true;
    }

    /**
     * @return last successfully authenticated user as UserDTO
     * */
    public UserDTO getCurrentUser() {
        return this.userDTO;
    }

    /**Get User Object with jpa api from database.
     *
     * @param username Username
     * @return UserDTO from Datebase if there is a User in the Database with given username, otherwise null.
     * @throws DatabaseUserException
     */
    private UserDTO getUserWithJPA(String username) throws DatabaseUserException {
        UserDTO userTmp;
        try {
            userTmp = repository.findUserByUsername(username);
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occured while trying to connect to database with JPA");
        }
        return userTmp;
    }
}


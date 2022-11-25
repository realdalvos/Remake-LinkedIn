package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.services.LoginServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginServiceInterface {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDTO userDTO = null;

    @Override
    public boolean authenticate(String username, String password) throws DatabaseUserException {
        UserDTO tmpUser = this.getUserWithJPA(username);

        if (tmpUser == null || !passwordEncoder.matches(password, tmpUser.getPassword().trim())) {
            return false;
        }
        this.userDTO = tmpUser;
        return true;
    }

    @Override
    public UserDTO getCurrentUser() {
        return this.userDTO;
    }

    /**Get User Object with jpa api from database.
     *
     * @param username Username
     * @return UserDTO from Datebase if there is a User in the Database with given username, otherwise null.
     * @throws DatabaseUserException Something went wrong with the Database
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

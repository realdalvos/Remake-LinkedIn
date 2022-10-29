package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.repository.UserRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginControlTest {
    @Autowired
    LoginControl loginControl = new LoginControl();
    @Autowired
    RegistrationControl registrationControl = new RegistrationControl();
    @Autowired
    UserRepository userRepository;

    @BeforeClass
    @DisplayName("Making sure that a user called \"JUnitTest\" exists, if not then create a new User.")
    void init(){
        UserDTO user = userRepository.findUserByUsername("JUnitTest");
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }

        User testUser = new User();
        testUser.setUsername("JUnitTest");
        testUser.setPassword("SicheresPasswort");
        testUser.setEmail("testUser@JUnitTest.de");
        testUser.setRole("student");

        //Decided to create a company, because when making a student the MatrikelNr might already be taken
        Company company = new Company();
        company.setName("JUnitTest");

        try {
            registrationControl.registerCompany(testUser, company);
        } catch (DatabaseUserException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    @BeforeEach
    @DisplayName("Making sure that a user called \"JUnitTest\" exists, if not then create a new User.")
    void createTestUser(){
        if(userRepository.findUserByUsername("JUnitTest") == null) {
            User testUser = new User();
            testUser.setUsername("JUnitTest");
            testUser.setPassword("SicheresPasswort");
            testUser.setEmail("testUser@JUnitTest.de");
            testUser.setRole("student");

            //Decided to create a company, because when making a student the MatrikelNr might already be taken
            Company company = new Company();
            company.setName("JUnitTest");

            try {
                registrationControl.registerCompany(testUser, company);
            } catch (DatabaseUserException e) {
                throw new RuntimeException(e);
            }
        }
    }

     */

    @Test
    void testAuthenticateShouldWork() throws DatabaseUserException {
        assertTrue(loginControl.authenticate("JUnitTest","SicheresPasswort"));
    }

    @Test
    void testAuthenticateShouldFail() throws DatabaseUserException {
        assertFalse(loginControl.authenticate("JUnitTest","FalschesPasswort"));
    }

    @Test
    void testGetCurrentUser() {
    }
}
package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.repository.UserRepository;
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

    @BeforeEach
    @DisplayName("Creating a user called \"JUnitTest\" exists.")
    void init(){
        deleteTestUser();

        //Create a new User called "JUnitTest"
        User testUser = new User();
        testUser.setUsername("JUnitTest");
        testUser.setPassword("SicheresPasswort");
        testUser.setEmail("testUser@JUnitTest.de");
        testUser.setRole("student");

        //Decided to create a company, because when making a student the MatrikelNr might already be taken
        Company company = new Company();
        company.setName("JUnitTest");

        //Save User to database
        try {
            registrationControl.registerCompany(testUser, company);
        } catch (DatabaseUserException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void after(){
       deleteTestUser();
    }

    private void deleteTestUser(){
        //If there exists a User called "JUnitTest" than delete this user.
        UserDTO user = userRepository.findUserByUsername("JUnitTest");
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }
    }

    @Test
    void testAuthenticateWhenNoSuchUser() throws DatabaseUserException {
        deleteTestUser();
        //Should fail since there is no such user
        assertFalse(loginControl.authenticate("JUnitTest","SicheresPasswort"));
    }

    @Test
    void testAuthenticateShouldWork() throws DatabaseUserException {
        //should work since there exists the user "JUnitTest" with given password
        assertTrue(loginControl.authenticate("JUnitTest","SicheresPasswort"));
    }

    @Test
    void testAuthenticateShouldFail() throws DatabaseUserException {
        //should fail since there exists the user "JUnitTest" but the password is wrong
        assertFalse(loginControl.authenticate("JUnitTest","FalschesPasswort"));
    }

    @Test
    void getCurrentUser() throws DatabaseUserException {
        loginControl.authenticate("JUnitTest","SicheresPasswort");
        //should return UserDTO of user JUnitTest
        assertNotNull(loginControl.getCurrentUser());
        assertEquals("JUnitTest", loginControl.getCurrentUser().getUsername());
    }
}

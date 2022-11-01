package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.util.Globals;
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
        UserDTOImpl testUser = new UserDTOImpl();
        testUser.setUsername("JUnitTest");
        testUser.setPassword("SicheresPasswort");
        testUser.setEmail("testUser@JUnitTest.de");
        testUser.setRole(Globals.Roles.company);

        //Decided to create a company, because when making a student the MatrikelNr might already be taken
        CompanyDTOImpl company = new CompanyDTOImpl();
        company.setName("JUnitTest");

        //Save User to database
        try {
            registrationControl.registerCompany(testUser, company);
        } catch (DatabaseUserException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    @DisplayName("Deleting user \"JUnitTest\".")
    void after(){
        deleteTestUser();
    }

    /**
     * Delete user "JUnitTest".*/
    private void deleteTestUser(){
        UserDTO user = userRepository.findUserByUsername("JUnitTest");
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }
    }

    @Test
    @DisplayName("LoginControl should return false since there is no user \"JUnitTest\".")
    void testAuthenticateWhenNoSuchUser() throws DatabaseUserException {
        deleteTestUser();

        assertFalse(loginControl.authenticate("JUnitTest","SicheresPasswort"));
    }

    @Test
    @DisplayName("LoginControl should return false since the password is not correct for user \"JUnitTest\".")
    void testAuthenticateShouldFail() throws DatabaseUserException {
        assertFalse(loginControl.authenticate("JUnitTest","FalschesPasswort"));
    }

    @Test
    @DisplayName("LoginControl should return true since the password is correct for user \"JUnitTest\".")
    void testAuthenticateShouldWork() throws DatabaseUserException {
        assertTrue(loginControl.authenticate("JUnitTest","SicheresPasswort"));
    }

    @Test
    @DisplayName("GetCurrentUser Method should return the last registered user, in this case user \"JUnitTest\".")
    void getCurrentUser() throws DatabaseUserException {
        loginControl.authenticate("JUnitTest","SicheresPasswort");

        //should return UserDTO of user JUnitTest
        UserDTO currentUser = loginControl.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("JUnitTest", currentUser.getUsername());
    }
}

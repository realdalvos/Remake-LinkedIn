package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginControlTest {
    @Autowired
    AuthorizationControl authorizationControl;
    @Autowired
    HelperForTests h;
    @Autowired
    UserDTO testUser;

    @BeforeEach
    @DisplayName("Registering a test company in the database called \"JUnitTest\" exists.")
    void init(){
        h.registerTestCompany();
        testUser = h.getUserDTOForCompany();
    }

    @AfterEach
    @DisplayName("Deleting user \"JUnitTest\".")
    void after(){
        h.deleteTestUsers();
    }

    @Test
    @DisplayName("LoginControl should return false since there is no user with given username.")
    void testAuthenticateWhenNoSuchUser() throws DatabaseUserException {
        h.deleteTestUsers();

        assertFalse(authorizationControl.authenticate(testUser.getUsername(), testUser.getPassword()));
    }

    @Test
    @DisplayName("LoginControl should return false since the password is correct.")
    void testAuthenticateShouldFail() throws DatabaseUserException {
        assertFalse(authorizationControl.authenticate(testUser.getUsername(),testUser.getPassword() + "Mehr Zeichen"));
    }

    @Test
    @DisplayName("LoginControl should return true since the password is correct for user \"JUnitTest\".")
    void testAuthenticateShouldWork() throws DatabaseUserException {
        assertTrue(authorizationControl.authenticate(testUser.getUsername(), testUser.getPassword()));
    }

    @Test
    @DisplayName("GetCurrentUser Method should return the last registered user, in this case user \"JUnitTest\".")
    void testGetCurrentUser() throws DatabaseUserException {
        authorizationControl.authenticate(testUser.getUsername(),testUser.getPassword());

        //should return UserDTO of user JUnitTest
        UserDTO currentUser = authorizationControl.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals(testUser.getUsername(), currentUser.getUsername());
    }
}

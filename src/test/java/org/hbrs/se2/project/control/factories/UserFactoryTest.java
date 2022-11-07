package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.util.Globals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {

    UserDTO testUser;

    @BeforeEach
    void setUp() {
        UserDTOImpl testUserImpl = new UserDTOImpl();
        testUserImpl.setUsername("JUnitTest");
        testUserImpl.setPassword("SicheresPasswort");
        testUserImpl.setEmail("testUser@JUnitTest.de");
        testUserImpl.setUserid(99999);
        testUserImpl.setRole(Globals.Roles.company);

        testUser = testUserImpl;
    }

    @Test
    @DisplayName("Testing if createUser-Method works as expected.")
    void testCreateUser() {
        User userFromFactory = UserFactory.createUser(testUser);
        assertNotNull(userFromFactory, "createUser Method should return a Instance of User and not null.");

        assertEquals(testUser.getUserid(), userFromFactory.getUserid(), "UserID should match.");
        assertEquals(testUser.getUsername(), userFromFactory.getUsername(), "Username should match.");
        assertEquals(testUser.getRole(), userFromFactory.getRole(), "Role should match.");
        assertEquals(testUser.getEmail(), userFromFactory.getEmail(), "Email should match.");
        assertEquals(testUser.getPassword(), userFromFactory.getPassword(), "Password should match.");
    }
}
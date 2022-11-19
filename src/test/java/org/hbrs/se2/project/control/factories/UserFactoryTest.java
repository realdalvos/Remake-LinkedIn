package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.hbrs.se2.project.util.Globals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {
    static EntityCreationService entityCreationService;
    UserDTO testUser;

    @BeforeAll
    static void init() {
        entityCreationService = new EntityCreationService();
    }

    @BeforeEach
    void setUp() {
        testUser = new UserDTOImpl("JUnitTest", "SicheresPasswort", "testUser@JUnitTest.de", Globals.Roles.company);
        testUser.setUserid(99999);
    }

    @Test
    @DisplayName("Testing if createUser-Method works as expected.")
    void testCreateUser() {
        User userFromFactory;
        userFromFactory = entityCreationService.userFactory(testUser).createEntity();
        assertNotNull(userFromFactory, "createUser Method should return a Instance of User and not null.");

        assertEquals(testUser.getUserid(), userFromFactory.getUserid(), "UserID should match.");
        assertEquals(testUser.getUsername(), userFromFactory.getUsername(), "Username should match.");
        assertEquals(testUser.getRole(), userFromFactory.getRole(), "Role should match.");
        assertEquals(testUser.getEmail(), userFromFactory.getEmail(), "Email should match.");
        assertEquals(testUser.getPassword(), userFromFactory.getPassword(), "Password should match.");
    }
}
package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.util.Globals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentFactoryTest {
    UserDTO testUser;
    StudentDTO testStudent;

    @BeforeEach
    void setUp() {
        UserDTOImpl testUserImpl = new UserDTOImpl();
        testUserImpl.setUsername("JUnitTest");
        testUserImpl.setPassword("SicheresPasswort");
        testUserImpl.setEmail("testUser@JUnitTest.de");
        testUserImpl.setUserid(99999);
        testUserImpl.setRole(Globals.Roles.company);

        testUser = testUserImpl;

        StudentDTOImpl testStudentImpl = new StudentDTOImpl();
        testStudentImpl.setStudentId(1);
        testStudentImpl.setFirstname("Test");
        testStudentImpl.setLastname("Student");
        testStudentImpl.setMatrikelnumber("1234");
        testStudentImpl.setUniversity("HRBS");
        testStudentImpl.setUserid(testUser.getUserid());
        testStudentImpl.setStudyMajor("Software Engineering");

        testStudent = testStudentImpl;
    }

    @Test
    @DisplayName("Checking if createStudent-Method works as expected.")
    void createStudent() {
        Student studentFromFactory = StudentFactory.createStudent(testStudent, testUser);
        assertNotNull(studentFromFactory, "createStudent Method should return a Instance of Student and not null.");

        assertEquals(testStudent.getMatrikelnumber(), studentFromFactory.getMatrikelnumber(), "Matrikelnumber should match.");
        assertEquals(testStudent.getFirstname(), studentFromFactory.getFirstname(), "First name should match.");
        assertEquals(testStudent.getLastname(), studentFromFactory.getLastname(), "Last name should match.");
        assertEquals(testStudent.getStudentid(), studentFromFactory.getStudentid(), "StudentID should match.");
        assertEquals(testStudent.getUserid(), studentFromFactory.getUserid(), "UserID should match.");
        assertEquals(testStudent.getUniversity(), studentFromFactory.getUniversity(), "University should match.");
        assertEquals(testStudent.getStudyMajor(), studentFromFactory.getStudyMajor(), "Study Major should match.");
    }
}
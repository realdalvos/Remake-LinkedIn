package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationControlTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RegistrationControl registrationControl;
    @Autowired
    HelperForTests h;

    @Autowired
    UserDTO userDTO;
    @Autowired
    StudentDTO studentDTO;
    @Autowired
    CompanyDTO companyDTO;

    @BeforeEach
    @DisplayName("Creating a user called \"JUnitTest\" exists.")
    void setUp() {
        userDTO = h.getUserDTOForCompany();
        companyDTO = h.getCompanyDTO();
        studentDTO = h.getStudentDTO();
    }

    @AfterEach
    @DisplayName("Deleting the user called \"JUnitTest\"")
    void tearDown() {
        h.deleteTestUsers();
    }

    @Test
    @DisplayName("Successful Registration for company")
    void registerCompanySuccess() {
        userDTO.setRole(Globals.Roles.company);
        assertDoesNotThrow(() -> registrationControl.registerCompany(userDTO, companyDTO));
        assertNotNull(userRepository.findByUsername(userDTO.getUsername()), "Can not find user in database after registration");
        assertNotNull(companyRepository.findByUserid(userRepository.findByUsername(userDTO.getUsername()).getUserid()), "Can not find company in database after registration");
    }

    @Test
    @DisplayName("Throw an error if an unique field in database already exists")
    void registerCompanyUnique() {
        userDTO.setRole(Globals.Roles.company);
        assertDoesNotThrow(() -> registrationControl.registerCompany(userDTO, companyDTO));
        UserDTO userDTOTmp = new UserDTOImpl();
        CompanyDTO companyDTOTmp = new CompanyDTOImpl();

        userDTOTmp.setUsername(userDTO.getUsername());
        DatabaseUserException thrown = assertThrows(DatabaseUserException.class, () -> registrationControl.registerCompany(userDTO, companyDTOTmp));
        assertEquals("Username already exists", thrown.getMessage());

        userDTOTmp.setUsername("");
        userDTOTmp.setEmail(userDTO.getEmail());
        thrown = assertThrows(DatabaseUserException.class, () -> registrationControl.registerCompany(userDTOTmp, companyDTOTmp));
        assertEquals("Email already exists", thrown.getMessage());
    }

    @Test
    @DisplayName("Successful Registration for student")
    void registerStudentSuccess() {
        userDTO.setRole(Globals.Roles.student);
        assertDoesNotThrow(() -> registrationControl.registerStudent(userDTO, studentDTO));
        assertNotNull(userRepository.findByUsername(userDTO.getUsername()), "Can not find user in database after registration");
        assertNotNull(studentRepository.findByUserid(userRepository.findByUsername(userDTO.getUsername()).getUserid()), "Can not find company in database after registration");
    }

    @Test
    @DisplayName("Throw an error if an unique field in database already exists")
    void registerStudentUnique() {
        userDTO.setRole(Globals.Roles.student);
        assertDoesNotThrow(() -> registrationControl.registerStudent(userDTO, studentDTO));
        UserDTO userDTOTmp = new UserDTOImpl();
        StudentDTO studentDTOTmp = new StudentDTOImpl();

        userDTOTmp.setUsername(userDTO.getUsername());
        DatabaseUserException thrown = assertThrows(DatabaseUserException.class, () -> registrationControl.registerStudent(userDTO, studentDTOTmp));
        assertEquals("Username already exists", thrown.getMessage());

        userDTOTmp.setUsername("");
        userDTOTmp.setEmail(userDTO.getEmail());
        thrown = assertThrows(DatabaseUserException.class, () -> registrationControl.registerStudent(userDTOTmp, studentDTOTmp));
        assertEquals("Email already exists", thrown.getMessage());

        userDTOTmp.setEmail("");
        studentDTOTmp.setMatrikelnumber(studentDTO.getMatrikelnumber());
        thrown = assertThrows(DatabaseUserException.class, () -> registrationControl.registerStudent(userDTOTmp, studentDTOTmp));
        assertEquals("Matrikelnumber already exists", thrown.getMessage());
    }
}
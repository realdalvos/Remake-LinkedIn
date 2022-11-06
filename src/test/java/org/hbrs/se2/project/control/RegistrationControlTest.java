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
    RegistrationControl registrationControl = new RegistrationControl();

    UserDTO userDTO;
    StudentDTO studentDTO;
    CompanyDTO companyDTO;
    String testString = "JUnitTest";

    @BeforeEach
    @DisplayName("Creating a user called \"JUnitTest\" exists.")
    void setUp() {
        tearDown();
        userDTO = new UserDTOImpl(testString, testString, testString, Globals.Roles.company);
        studentDTO = new StudentDTOImpl(userDTO.getUserid(), testString, testString, "123654", testString, testString);
        companyDTO = new CompanyDTOImpl(userDTO.getUserid(), testString, testString, false, testString);

        companyDTO.setName(testString);
    }

    @AfterEach
    @DisplayName("Deleting the user called \"JUnitTest\"")
    void tearDown() {
        UserDTO user = userRepository.findUserByUsername(testString);
        if(user != null) {
            userRepository.deleteById(user.getUserid());

            CompanyDTO comp = companyRepository.findCompanyByUserid(user.getUserid());
            StudentDTO stud = studentRepository.findStudentByUserid(user.getUserid());

            if (comp != null) {
                companyRepository.deleteById(comp.getCompanyid());
            }
            if (stud != null) {
                studentRepository.deleteById(stud.getStudentid());
            }
        }
    }

    @Test
    @DisplayName("Successful Registration for company")
    void registerCompanySuccess() {
        userDTO.setRole(Globals.Roles.company);
        assertDoesNotThrow(() -> registrationControl.registerCompany(userDTO, companyDTO));
        assertNotNull(userRepository.findUserByUsername(testString), "Can not find user in database after registration");
        assertNotNull(companyRepository.findCompanyByName(testString), "Can not find company in database after registration");
    }

    @Test
    @DisplayName("Throw an error if a unique field in database already exists")
    void registerCompanyUnique() {
        userDTO.setRole(Globals.Roles.company);
        assertDoesNotThrow(() -> registrationControl.registerCompany(userDTO, companyDTO));
        UserDTO userDTOTmp = new UserDTOImpl();

        userDTOTmp.setUsername(testString);
        DatabaseUserException thrown = assertThrows(DatabaseUserException.class, () -> registrationControl.registerCompany(userDTO, companyDTO));
        assertEquals("Username already exists", thrown.getMessage());

        userDTOTmp.setUsername("");
        userDTOTmp.setEmail(testString);
        thrown = assertThrows(DatabaseUserException.class, () -> registrationControl.registerCompany(userDTOTmp, companyDTO));
        assertEquals("Email already exists", thrown.getMessage());
    }

    @Test
    @DisplayName("TBD")
    void registerStudentSuccess() {

    }

    @Test
    @DisplayName("TBD")
    void registerStudentUnique() {

    }
}
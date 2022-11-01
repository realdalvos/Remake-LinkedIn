package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationControlTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegistrationControl registrationControl;

    UserDTO userDTO1;
    StudentDTO studentDTO1;
    CompanyDTO companyDTO1;
    String username;

    @BeforeEach
    void setUp() {
        registrationControl = new RegistrationControl();
        userDTO1 = new UserDTOImpl();
        studentDTO1 = new StudentDTOImpl();
        companyDTO1 = new CompanyDTOImpl();

        username = "RegisterTest1";
        userDTO1.setUserid(999);
        userDTO1.setUsername(username);
        userDTO1.setPassword("Password");
        userDTO1.setEmail("register1@test.com");
        userDTO1.setRole("company");

        companyDTO1.setName("RegisterTest1");
    }

    @AfterEach
    void tearDown() {
        UserDTO user = userRepository.findUserByUsername(username);
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }
    }

    @Test
    void registerStudent() {
        fail("Not yet implemented.");
    }

    @Test
    void registerCompany() {
        registrationControl.registerCompany(userDTO1, companyDTO1);
        assertNotNull(userRepository.findUserByUsername(username));
    }

    @Test
    void getCurrentUser() {
    }

    @Test
    void checkFormInputStudent() {
        fail("Not yet implemented.");
    }

    @Test
    void checkFormInputCompany() {
    }

    @Test
    void checkPasswordConfirmation() {
    }
}
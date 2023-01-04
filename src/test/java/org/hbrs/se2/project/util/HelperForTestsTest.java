package org.hbrs.se2.project.util;

import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelperForTestsTest {

    @Autowired
    HelperForTests h;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserControl userControl;

    @Test
    @DisplayName("Testing registerTestCompany() function")
    void test_registerTestCompany() {
        assertDoesNotThrow(() -> {h.registerTestCompany();});

        UserDTO defaultUser = h.getUserDTOForCompany();

        assertNotNull( companyRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername()).getUserid()) );

        assertDoesNotThrow(() -> {h.registerTestStudent();});
        assertNotNull( companyRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername()).getUserid()) );
    }

    @Test
    @DisplayName("Testing registerTestCompany(int n) function")
    void test_multiple_registerTestCompany() {
        int n = 3;

        assertDoesNotThrow(() -> {h.registerTestCompanies(n);});

        UserDTO defaultUser = h.getUserDTOForCompany();
        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( companyRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }

        //should not throw when registering again even if there are already entities occupying the unique values
        assertDoesNotThrow(() -> {h.registerTestCompanies(3);});

        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( companyRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }
    }

    @Test
    @DisplayName("Testing registerTestCompany() function")
    void test_registerTestStudent() {
        assertDoesNotThrow(() -> {h.registerTestStudent();});

        UserDTO defaultUser = h.getUserDTOForStudent();

        assertNotNull( studentRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername()).getUserid()) );

        assertDoesNotThrow(() -> {h.registerTestStudent();});

        assertNotNull( studentRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername()).getUserid()) );
    }

    @Test
    @DisplayName("Testing registerTestCompanies(int n) function")
    void test_multiple_registerTestCompanies() {
        int n = 3;

        assertDoesNotThrow(() -> {h.registerTestStudents(3);});

        UserDTO defaultUser = h.getUserDTOForStudent();

        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( studentRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }

        //should not throw when registering again even if there are already entities occupying the unique values
        assertDoesNotThrow(() -> {h.registerTestStudents(3);});

        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( studentRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }
    }

    @Test
    @DisplayName("Function deleteRegisteredTestCompanies() should remove all with registerTestCompanies(int n) registered test companies from the repository")
    void deleteRegisteredTestCompanies_multiple() {
        int n = 3;

        assertDoesNotThrow(() -> {h.registerTestCompanies(3);});

        UserDTO defaultUser = h.getUserDTOForCompany();

        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( companyRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }

        assertDoesNotThrow(() -> h.deleteRegisteredTestCompanies());

        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNull( userRepository.findByUsername(defaultUser.getUsername() + suffix) );
        }
    }

    @Test
    @DisplayName("Function deleteRegisteredTestCompanies() should remove the with registerTestCompany() registered test company from the repository")
    void deleteRegisteredTestCompanies_single() {

        assertDoesNotThrow(() -> {h.registerTestCompany();});

        UserDTO defaultUser = h.getUserDTOForCompany();

        assertNotNull( companyRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername()).getUserid()) );

        assertDoesNotThrow(() -> h.deleteRegisteredTestCompanies());

        assertNull( userRepository.findByUsername(defaultUser.getUsername()) );
    }

    @Test
    @DisplayName("Function deleteRegisteredTestStudents() should remove all with registerTestStudents(int n) registered test students from the repository")
    void deleteRegisteredTestStudents_multiple() {
        int n = 3;

        assertDoesNotThrow(() -> {h.registerTestStudents(n);});

        UserDTO defaultUser = h.getUserDTOForStudent();

        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( studentRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }

        assertDoesNotThrow(() -> h.deleteRegisteredTestStudents());

        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNull( userRepository.findByUsername(defaultUser.getUsername() + suffix) );
        }
    }

    @Test
    @DisplayName("Function deleteRegisteredTestStudents() should remove the with registerTestStudent() registered test student from the repository")
    void deleteRegisteredTestStudents_single() {

        assertDoesNotThrow(() -> {h.registerTestStudent();});

        UserDTO defaultUser = h.getUserDTOForStudent();

        assertNotNull( studentRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername()).getUserid()) );

        assertDoesNotThrow(() -> h.deleteRegisteredTestStudents());

        assertNull( userRepository.findByUsername(defaultUser.getUsername()) );
    }

    @Test
    void deleteTestUsers() {
        int n = 3;

        h.registerTestStudents(n);
        h.registerTestCompanies(n);

        UserDTO defaultUser = h.getUserDTOForStudent();
        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( studentRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }

        defaultUser = h.getUserDTOForCompany();
        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNotNull( companyRepository.findByUserid(userRepository.findByUsername(defaultUser.getUsername() + suffix).getUserid()) );
        }

        h.deleteTestUsers();

        defaultUser = h.getUserDTOForStudent();
        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNull( userRepository.findByUsername(defaultUser.getUsername() + suffix) );
        }

        defaultUser = h.getUserDTOForCompany();
        for(int i = 0; i < n; i++){
            String suffix = (i == 0) ? "" : "" + (i+1);
            assertNull( userRepository.findByUsername(defaultUser.getUsername() + suffix) );
        }
    }
}
package org.hbrs.se2.project.util;

import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelperForTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    RegistrationControl registrationControl;

    private UserDTO testUserForCompany = new UserDTOImpl("TestUserCompany", "SicheresPasswort", "testUser@JUnitTest.de", Globals.Roles.company);
    private CompanyDTOImpl testCompany = new CompanyDTOImpl(0, "TestCompany", "Testindustry", false, "Testdetails");
    private UserDTO testUserForStudent = new UserDTOImpl("TestUserStudent", "SicheresPasswort", "testUser2@JUnitTest.de", Globals.Roles.student);
    private StudentDTO testStudent = new StudentDTOImpl(0, "Stan", "Student", "123456", "HBRS");

    /**<pre>
     * Registering the test company and returning a CompanyDTO of the registered company.
     * If needed, you can get the raw CompanyDTO with getCompanyDTO() and the corresponding raw UserDTO with getUserDTOForCompany().
     * NOTE: Since the company was saved in the database the user id will be set (So if you want the corresponding user from the database u can find him by this user id).
     * @return CompanyDTO of the test company.
     * </pre>*/
    public CompanyDTO registerTestCompany(){
        deleteTestCompany();

        //Save User to database
        try {
            registrationControl.registerCompany(getUserDTOForCompany(), getCompanyDTO());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserDTO u = userRepository.findUserByUsername(testUserForCompany.getUsername());
        return companyRepository.findCompanyByUserid(u.getUserid());
    }

    /**<pre>
     * Registering the test student and returning a StudentDTO of the registered student.
     * If needed, you can get the raw StudentDTO with getStudentDTO() and the corresponding raw UserDTO with getUserDTOForStudent().
     * NOTE: Since the student was saved in the database the user id will be set (So if you want the corresponding user from the database u can find him by this user id).
     * @return StudentDTO of the test company.
     * </pre>*/
    public StudentDTO registerTestStudent(){
        deleteTestStudent();

        //Save User to database
        try {
            registrationControl.registerStudent(getUserDTOForStudent(), getStudentDTO());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserDTO u = userRepository.findUserByUsername(testUserForStudent.getUsername());
        return studentRepository.findStudentByUserid(u.getUserid());
    }

    /**
     * Returning the unchanged UserDTO corresponding to the test company.
     * NOTE: user ID will be 0.*/
    public UserDTO getUserDTOForCompany(){
        return clone(testUserForCompany);
    }

    /**
     * Returning the unchanged CompanyDTO of the test company.
     * NOTE: company ID will be 0.*/
    public CompanyDTO getCompanyDTO() {
        return clone(testCompany);
    }

    /**
     * Returning the unchanged UserDTO corresponding to the test student.
     * NOTE: user ID will be 0.*/
    public UserDTO getUserDTOForStudent(){
        return clone(testUserForStudent);
    }

    /**
     * Returning the unchanged StudentDTO of the test student.
     * NOTE: student ID will be 0.*/
    public StudentDTO getStudentDTO() {
        return clone(testStudent);
    }

    /**
     * Deleting the test company from the database.*/
    public void deleteTestCompany() {
        deleteUsersOccupyingUniques(getUserDTOForCompany());
    }

    /**
     * Deleting the test student from the database.*/
    public void deleteTestStudent() {
        deleteUsersOccupyingUniques(getUserDTOForStudent());

        //Deleting user that occupies MatrikelNr
        StudentDTO student = studentRepository.findStudentByMatrikelnumber(getStudentDTO().getMatrikelnumber());
        if(student != null) {
            UserDTO user = userRepository.findUserByUserid(student.getUserid());
            userRepository.deleteById(user.getUserid());
        }
    }

    /**
     * Since Username and Email might already be taken we have to delete those users to make space for our test user. */
    private void deleteUsersOccupyingUniques(UserDTO u){
        //Deleting user that occupies Username
        UserDTO user = userRepository.findUserByUsername(u.getUsername());
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }

        //Deleting user that occupies Email
        user = userRepository.findUserByEmail(u.getEmail());
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }
    }

    /**
     * Deleting the test students and companies from the database.*/
    public void deleteTestUsers(){
       deleteTestCompany();
       deleteTestStudent();
    }

    /*
     * Since this Helper was meant for tests, the UserDTO should always be the same (When registering a student or company the password gets hashed and thereby changed, which alters the original UserDTO).*/
    private UserDTO clone(UserDTO u){
        return new UserDTOImpl(u.getUsername(), u.getPassword(), u.getEmail(), u.getRole());
    }

    /*
     * Same concerns as in the clone Method for users.*/
    private CompanyDTO clone(CompanyDTO c){
        return new CompanyDTOImpl(c.getUserid(), c.getName(), c.getIndustry(), c.getBanned(), c.getContactdetails());
    }

    /*
     * Same concerns as in the clone Method for users.*/
    private StudentDTO clone(StudentDTO s){
        return new StudentDTOImpl(s.getUserid(), s.getFirstname(), s.getFirstname(), s.getMatrikelnumber(), s.getUniversity());
    }
}

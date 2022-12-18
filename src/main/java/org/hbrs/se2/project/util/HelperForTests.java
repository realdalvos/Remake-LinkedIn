package org.hbrs.se2.project.util;

import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.views.studentViews.StudentProfileView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelperForTests {
    final UserRepository userRepository;
    final CompanyRepository companyRepository;
    final StudentRepository studentRepository;
    final RegistrationControl registrationControl;

    private UserDTO testUserForCompany = new UserDTOImpl("TestUserCompany", "SicheresPasswort", "testUser@JUnitTest.de", Globals.Roles.company);
    private CompanyDTOImpl testCompany = new CompanyDTOImpl(0, "TestCompany", "Testindustry", false);
    private UserDTO testUserForStudent = new UserDTOImpl("TestUserStudent", "SicheresPasswort", "testUser2@JUnitTest.de", Globals.Roles.student);
    private StudentDTO testStudent = new StudentDTOImpl(0, "Stan", "Student", "123456", "HBRS");

    private List<CompanyDTO> registeredCompanies = new ArrayList<>();

    private List<StudentDTO> registeredStudents = new ArrayList<>();

    public HelperForTests(UserRepository userRepository, CompanyRepository companyRepository, StudentRepository studentRepository, RegistrationControl registrationControl) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.registrationControl = registrationControl;
    }

    /**<pre>
     * Registering the test company and returning a CompanyDTO of the registered company.
     * If needed, you can get the raw CompanyDTO with getCompanyDTO() and the corresponding raw UserDTO with getUserDTOForCompany().
     * NOTE: Since the company was saved in the database the user id will be set (So if you want the corresponding user from the database u can find him by this user id).
     * NOTE: Already registered test company created with registerTestCompany() will be deleted.
     * @return CompanyDTO of the test company.
     * </pre>*/
    public CompanyDTO registerTestCompany(){
        return registerTestCompanies(1).get(0);
    }

    /**
     * A way to register multiple companies.
     * NOTE: All already registered test companies created with registerTestCompany() or registerTestCompany(int) will be deleted.
     * @param n number of companies.
     * @return List of Companies.*/
    public List<CompanyDTO> registerTestCompanies(int n){
        ArrayList<CompanyDTO> list = new ArrayList<>();
        if(n < 1){
            return list;
        }

        for(int i = 0; i < n; i++){
            UserDTO tmp = getUserDTOForCompany();
            //first user should be named TestUserForCompany, following users should be named TestUserForCompany1,...
            String suffix = (i == 0) ? "" : "" + (i + 1);

            UserDTOImpl testUser = new UserDTOImpl(tmp.getUsername() + suffix, tmp.getPassword() + suffix, tmp.getEmail() + suffix, tmp.getRole());

            CompanyDTO tmpC = getCompanyDTO();
            CompanyDTOImpl testCompany = new CompanyDTOImpl(0, tmpC.getName() + suffix, tmpC.getIndustry() + suffix, false);

            //make space for user
            deleteUsersOccupyingUniques(testUser);

            //Save User to database
            try {
                registrationControl.registerCompany(testUser, testCompany);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //adding company to list of registered companies
            list.add(companyRepository.findByUserid((userRepository.findByUsername(testUser.getUsername())).getUserid()));
        }

        this.registeredCompanies = list;
        return list;
    }

    /**<pre>
     * Registering the test student and returning a StudentDTO of the registered student.
     * If needed, you can get the raw StudentDTO with getStudentDTO() and the corresponding raw UserDTO with getUserDTOForStudent().
     * NOTE: Since the student was saved in the database the user id will be set (So if you want the corresponding user from the database u can find him by this user id).
     * @return StudentDTO of the test company.
     * </pre>*/
    public StudentDTO registerTestStudent(){
        return registerTestStudents(1).get(0);
    }

    /**
     * A way to register multiple students.
     * NOTE: All already registered test students created with registerTestStudent() or registerTestStudent(int) will be deleted.
     * @param n number of companies.
     * @return List of Companies.*/
    public List<StudentDTO> registerTestStudents(int n){
        ArrayList<StudentDTO> list = new ArrayList<>();
        if(n < 1){
            return list;
        }

        for(int i = 0; i < n; i++){
            UserDTO tmp = getUserDTOForStudent();
            //first user should be named TestUserForStudent, following users should be named TestUserForStudent1,...
            String suffix = (i == 0) ? "" : "" + (i + 1);

            UserDTOImpl testUser = new UserDTOImpl(tmp.getUsername() + suffix, tmp.getPassword() + suffix, tmp.getEmail() + suffix, tmp.getRole());

            StudentDTO tmpS = getStudentDTO();
            StudentDTOImpl testStudent = new StudentDTOImpl(0, tmpS.getFirstname() + suffix, tmpS.getLastname() + suffix, tmpS.getMatrikelnumber() + suffix, tmpS.getUniversity() + suffix);

            //make space for user
            deleteUsersOccupyingUniques(testUser);

            //make space for student
            deleteStudentsOccupyingUniques(testStudent);

            //Save User to database
            try {
                registrationControl.registerStudent(testUser, testStudent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //adding company to list of registered companies
            list.add(studentRepository.findByUserid((userRepository.findByUsername(testUser.getUsername())).getUserid()));
        }

        this.registeredStudents = list;
        return list;
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
     * Deleting the registered test companies from the database.*/
    public void deleteRegisteredTestCompanies() {
        for(CompanyDTO c : registeredCompanies){
            UserDTO tmp = userRepository.findByUserid(c.getUserid());
            deleteUsersOccupyingUniques(tmp);
        }
        /*Has to be deleted explicitly.
        * Some tests register this particular testUser themselves and this method should delete this user.*/
        deleteUsersOccupyingUniques(testUserForCompany);
        registeredCompanies = new ArrayList<>();
    }

    /**
     * Deleting the test student from the database.*/
    public void deleteRegisteredTestStudents() {
        for(StudentDTO s : registeredStudents){
            UserDTO tmp = userRepository.findByUserid(s.getUserid());
            deleteUsersOccupyingUniques(tmp);
            deleteStudentsOccupyingUniques(s);
        }
        /*Has to be deleted explicitly.
         * Some tests register this particular testUser themselves and this method should delete this user.*/
        deleteUsersOccupyingUniques(testUserForStudent);
        deleteStudentsOccupyingUniques(testStudent);
        registeredStudents = new ArrayList<>();
    }

    /**Since students have the unique MatrikelNr you have to make space before registering.*/
    private void deleteStudentsOccupyingUniques(StudentDTO s){
        //Deleting user that occupies MatrikelNr
        StudentDTO student = studentRepository.findByMatrikelnumber(s.getMatrikelnumber());
        if(student != null) {
            UserDTO user = userRepository.findByUserid(student.getUserid());
            userRepository.deleteById(user.getUserid());
        }
    }

    /**
     * Since Username and Email might already be taken we have to delete those users to make space for our test user. */
    private void deleteUsersOccupyingUniques(UserDTO u){
        //Deleting user that occupies Username
        UserDTO user = userRepository.findByUsername(u.getUsername());
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }

        //Deleting user that occupies Email
        user = userRepository.findByEmail(u.getEmail());
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }
    }

    /**
     * Deleting the registered test students and companies from the database.*/
    public void deleteTestUsers(){
       deleteRegisteredTestCompanies();
       deleteRegisteredTestStudents();
    }

    /*
     * Since this Helper was meant for tests, the UserDTO should always be the same (When registering a student or company the password gets hashed and thereby changed, which alters the original UserDTO).*/
    private UserDTO clone(UserDTO u){
        return new UserDTOImpl(u.getUsername(), u.getPassword(), u.getEmail(), u.getRole());
    }

    /*
     * Same concerns as in the clone Method for users.*/
    private CompanyDTO clone(CompanyDTO c){
        return new CompanyDTOImpl(c.getUserid(), c.getName(), c.getIndustry(), c.getBanned());
    }

    /*
     * Same concerns as in the clone Method for users.*/
    private StudentDTO clone(StudentDTO s){
        return new StudentDTOImpl(s.getUserid(), s.getFirstname(), s.getFirstname(), s.getMatrikelnumber(), s.getUniversity());
    }
}

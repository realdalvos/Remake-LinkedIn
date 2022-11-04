package org.hbrs.se2.project.views.studentViews.services.impl;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.control.factories.CompanyFactory;
import org.hbrs.se2.project.control.factories.StudentFactory;
import org.hbrs.se2.project.control.factories.UserFactory;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.views.studentViews.services.RegistrationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

public class RegistrationService implements RegistrationServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public boolean registerStudent(UserDTO user, StudentDTO student) throws Exception {
        // check if username, email, matrikelnumber already exists
        UserDTO userDTO = userRepository.findUserByUsername(user.getUsername());
        UserDTO userDTO1 = userRepository.findUserByEmail(user.getEmail());
        StudentDTO studentDTO = studentRepository.findStudentByMatrikelnumber(student.getMatrikelnumber());

        if(userDTO != null) {
            throw new Exception("Username already exists.");
        } else if(userDTO1 != null) {
            throw new Exception("Email already exists");
        } else if(studentDTO != null) {
            throw new Exception("Matrikelnumber already exists.");
        }

        // create new user of role "student"
        this.createAccount(user);
        // get User id from new saved user in db, so we can assign the userid to the data in student table
        UserDTO newSavedUser = userRepository.findUserByUsername(user.getUsername());
        // create student profile
        this.createStudentProfile(student, newSavedUser);
        return true;
    }

    @Override
    public boolean registerCompany(UserDTO user, CompanyDTO company) throws Exception {
        // check if username or email already exists
        UserDTO userDTO = userRepository.findUserByUsername(user.getUsername());
        UserDTO userDTO1 = userRepository.findUserByEmail(user.getEmail());

        if(userDTO != null) {
            throw new Exception("Username already exists");
        } else if(userDTO1 != null) {
            throw new Exception("Email already exists");
        }

        // create new user of role "company"
        this.createAccount(user);
        // get User id from new saved user in db
        UserDTO newSavedUser = userRepository.findUserByUsername(user.getUsername());
        // create company profile
        this.createCompanyProfile(company, newSavedUser);
        return true;
    }


    /**
     * Create a new account in the user database
     * @param userDTO User DTO to create user for
     * @throws DatabaseUserException Something went wrong in the database
     */
    private void createAccount(UserDTO userDTO) throws DatabaseUserException {
        try {
            //Saving user in db
            this.userRepository.save(UserFactory.createUser(userDTO));
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a user account in the database at createAccount");
        }
    }

    /**
     * Create a new account in the student database and connect it to the user
     * @param studentDTO Student DTO to be saved
     * @param userDTO User DTO to be saved
     * @throws DatabaseUserException Something went wrong in the database
     */
    private void createStudentProfile(StudentDTO studentDTO, UserDTO userDTO) throws DatabaseUserException {
        try {
            this.studentRepository.save(StudentFactory.createStudent(studentDTO, userDTO));
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Student Profile into the database at createStudentProfile");
        }
    }

    /**
     * Create a new Company profile and links it to the company owner
     * @param companyDTO Company DTO to be saved
     * @param userDTO User DTO of company Owner to be saved
     * @throws DatabaseUserException Something went wrong in the database
     */
    private void createCompanyProfile(CompanyDTO companyDTO, UserDTO userDTO) throws DatabaseUserException {
        try {
            this.companyRepository.save(CompanyFactory.createCompany(companyDTO, userDTO));
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Company Profile into the database at createCompanyProfile");
        }
    }
}

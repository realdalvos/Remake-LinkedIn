package org.hbrs.se2.project.control;

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
import org.hbrs.se2.project.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationControl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    // register new student by creating an account and a student profile
    // and save account data in user table and student data in student table
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

    // register new company by creating an account and a company profile
    // and save account data in user table and company data in student table
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

    private void createAccount(UserDTO userDTO) throws DatabaseUserException {
        try {
            //Saving user in db
            this.userRepository.save(UserFactory.createUser(userDTO));
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a user account in the database at createAccount");
        }
    }

    private void createStudentProfile(StudentDTO studentDTO, UserDTO userDTO) throws DatabaseUserException {
        try {
            this.studentRepository.save(StudentFactory.createStudent(studentDTO, userDTO));
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Student Profile into the database at createStudentProfile");
        }
    }

    private void createCompanyProfile(CompanyDTO companyDTO, UserDTO userDTO) throws DatabaseUserException {
        try {
            this.companyRepository.save(CompanyFactory.createCompany(companyDTO, userDTO));
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Company Profile into the database at createCompanyProfile");
        }
    }
}


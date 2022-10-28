package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class RegistrationControl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDTO userDTO = null;
    private StudentDTO student = null;
    private CompanyDTO company = null;

    // register new student by creating an account and a student profile
    // and save account data in user table and student data in student table
    public boolean registerStudent(User user, Student student) throws DatabaseUserException {
        this.createAccount(user);
        // get User id from new saved user in db
        // so we can assign the userid to the data in student table
        UserDTO userDTO1 = userRepository.findUserByUsername(user.getUsername());
        student.setUserid(userDTO1.getUserid());
        // create student profile
        try {
            try {
                this.createStudentProfile(student);
            } catch (DatabaseUserException e) {
                throw new DatabaseUserException("Student Profile could not be created at createStudentProfile");
            }
        } catch (RuntimeException ex) {
            // Delete user table data because student data could not be saved
            // so there does nat exist user data without student data
            // both has to exist
            System.out.println("Student profile could not be saved why user data was deleted.");
            userRepository.deleteById(userDTO1.getUserid());
            throw new RuntimeException("Matrikelnumber already exists.");
        }
        return true;
    };

    // register new company by creating an account and a company profile
    // and save account data in user table and company data in student table
    public boolean registerCompany(User user, Company company) throws DatabaseUserException {
        this.createAccount(user);
        // get User id from new saved user in db
        UserDTO userDTO2 = userRepository.findUserByUsername(user.getUsername());
        company.setUserid(userDTO2.getUserid());

        // create company profile
        try {
            try {
                this.createCompanyProfile(company);
            } catch (DatabaseUserException e) {
                throw new DatabaseUserException("Company Profile could not be created at registerCompany");
            }
        } catch (RuntimeException ex) {
            // Delete user table data because company data could not be saved
            // so there does nat exist user data without company data
            // both has to exist
            System.out.println("Company profile could not be saved why user data was deleted.");
            userRepository.deleteById(userDTO2.getUserid());
            throw new RuntimeException("Company Profile could not be created at registerCompany");
        }
        return true;
    }

    public UserDTO getCurrentUser() {return this.userDTO;}

    private void createAccount(User user) throws DatabaseUserException {
        try {
            //Hashing password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //Saving user with hashed password in db
            this.userRepository.save(user);
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a user account in the database at createAccount");
        }
    }

    private void createStudentProfile(Student student) throws DatabaseUserException {
        try {
            this.studentRepository.save(student);
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Student Profile into the database at createStudentProfile");
        }
    }

    private void createCompanyProfile(Company company) throws DatabaseUserException {
        try {
            this.companyRepository.save(company);
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Company Profile into the database at createCompanyProfile");
        }
    }

    // makes an array and calls function to check if input is null or empty "" for student
    public boolean checkFormInputStudent(
            String username, String password, String email,
            String firstname, String lastname
    ) {
        String[] array = {username, password, email, firstname, lastname};
        return Utils.checkIfInputEmpty(array);
    }

    // makes array and calls function to check if input is null or emtpy "" for company
    public boolean checkFormInputCompany(String username, String password, String email, String name) {
        String[] array = {username, password, email, name};
        return Utils.checkIfInputEmpty(array);
    }

    // Checks if password and confirmPassword are equal
    public boolean checkPasswordConfirmation(String passw1, String passw2) {
        return passw1.trim().equals(passw2.trim());
    }
}


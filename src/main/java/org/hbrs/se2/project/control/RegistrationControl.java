package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.control.factories.CompanyFactory;
import org.hbrs.se2.project.control.factories.StudentFactory;
import org.hbrs.se2.project.control.factories.UserFactory;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.entities.User;
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
    public boolean registerStudent(UserDTO user, StudentDTO student) throws DatabaseUserException {
        // create new user of role "student"
        this.createAccount(user, Globals.Roles.student);
        // get User id from new saved user in db so we can assign the userid to the data in student table
        UserDTO newSavedUser = userRepository.findUserByUsername(user.getUsername());
        // create student profile
        try {
            try {
                this.createStudentProfile(student, newSavedUser);
            } catch (DatabaseUserException e) {
                throw new DatabaseUserException("Student Profile could not be created at createStudentProfile");
            }
        } catch (RuntimeException ex) {
            // Delete user table data because student data could not be saved
            // so there does nat exist user data without student data
            // both has to exist
            System.out.println("Student profile could not be saved why user data was deleted.");
            userRepository.deleteById(newSavedUser.getUserid());
            throw new RuntimeException("Matrikelnumber already exists.");
        }
        return true;
    }

    // register new company by creating an account and a company profile
    // and save account data in user table and company data in student table
    public boolean registerCompany(UserDTO user, CompanyDTO company) throws DatabaseUserException {
        // create new user of role "company"
        this.createAccount(user, Globals.Roles.company);
        // get User id from new saved user in db
        UserDTO newSavedUser = userRepository.findUserByUsername(user.getUsername());
        // create company profile
        try {
            try {
                this.createCompanyProfile(company, newSavedUser);
            } catch (DatabaseUserException e) {
                throw new DatabaseUserException("Company Profile could not be created at registerCompany");
            }
        } catch (RuntimeException ex) {
            // Delete user table data because company data could not be saved
            // so there does nat exist user data without company data
            // both has to exist
            System.out.println("Company profile could not be saved why user data was deleted.");
            userRepository.deleteById(newSavedUser.getUserid());
            throw new RuntimeException("Company Profile could not be created at registerCompany");
        }
        return true;
    }

    private void createAccount(UserDTO userDTO, String role) throws DatabaseUserException {
        try {
            //Saving user in db
            this.userRepository.save(UserFactory.createUser(userDTO, role));
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


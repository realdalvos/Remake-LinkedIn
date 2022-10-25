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

    private UserDTO userDTO = null;
    private StudentDTO student = null;
    private CompanyDTO company = null;

    public boolean registerStudent(User user, Student student) throws DatabaseUserException {
        this.createAccount(user);
        // get User id from new saved user in db
        UserDTO userDTO1 = userRepository.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        student.setUserid(userDTO1.getUserid());

        System.out.println(userDTO1.getUserid() + " " + userDTO1.getUsername());
        System.out.println(student.getUserid());
        System.out.println(student.getFirstname());
        System.out.println(student.getLastname());
        System.out.println(student.getUniversity());
        System.out.println(student.getStudyMajor());
        System.out.println(student.getMatrikelnumber());

        this.createStudentProfile(student);
        return true;
    };

    public boolean registerCompany(User user, Company company) throws DatabaseUserException {
        this.createAccount(user);
        UserDTO userDTO2 = userRepository.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        company.setUserid(userDTO2.getUserid());

        System.out.println(userDTO2.getUserid() + " " + userDTO2.getUsername());
        this.createCompanyProfile(company);
        return true;
    }

    public UserDTO getCurrentUser() {return this.userDTO;}

    private void createAccount(User user) throws DatabaseUserException {
        try {
            this.userRepository.save(user);
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a user account in the database");
        }
    }

    private void createStudentProfile(Student student) throws DatabaseUserException {
        try {
            this.studentRepository.save(student);
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Student Profile into the database");
        }
    }

    private void createCompanyProfile(Company company) throws DatabaseUserException {
        try {
            this.companyRepository.save(company);
        } catch(org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occurred while saving a Company Profile into the database");
        }
    }
}


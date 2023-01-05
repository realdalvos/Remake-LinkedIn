package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.services.impl.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RegistrationControl {

    @Autowired
    RegistrationService registrationService;

    /**
     * Create a new Student and save their profile to the database
     * @param user User to be registered
     * @param student Student profile to be created
     * @throws DatabaseUserException Data already exists
     */
    public void registerStudent(UserDTO user, StudentDTO student) throws DatabaseUserException {
        registrationService.registerStudent(user, student);
    }

    /**
     * Create a new Company and save it to the database
     * @param user User the company belongs to
     * @param company The new Company to be created
     * @throws DatabaseUserException Data already exists
     */
    public void registerCompany(UserDTO user, CompanyDTO company) throws DatabaseUserException {
        registrationService.registerCompany(user, company);
    }

}

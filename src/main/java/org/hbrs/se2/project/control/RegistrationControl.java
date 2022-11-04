package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.views.studentViews.services.impl.RegistrationService;
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
     * @return True if successful
     * @throws Exception Data already exists
     */
    public boolean registerStudent(UserDTO user, StudentDTO student) throws Exception {
        return registrationService.registerStudent(user, student);
    }

    /**
     * Create a new Company and save it to the database
     * @param user User the company belongs to
     * @param company The new Company to be created
     * @return True if successful
     * @throws Exception Data already exists
     */
    public boolean registerCompany(UserDTO user, CompanyDTO company) throws Exception {
        return registrationService.registerCompany(user, company);
    }


}


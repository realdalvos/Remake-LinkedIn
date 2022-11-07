package org.hbrs.se2.project.services;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;

public interface RegistrationServiceInterface {

    /**
     * Create a new Student and save their profile to the database
     * @param user User to be registered
     * @param student Student profile to be created
     * @throws Exception Data already exists
     */
    void registerStudent(UserDTO user, StudentDTO student) throws Exception;

    /**
     * Create a new Company and save it to the database
     * @param user User the company belongs to
     * @param company The new Company to be created
     * @throws Exception Data already exists
     */
    void registerCompany(UserDTO user, CompanyDTO company) throws Exception;
}

package org.hbrs.se2.project.views.studentViews.services;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;

public interface RegistrationServiceInterface {

    public boolean registerStudent(UserDTO user, StudentDTO student) throws Exception;
    public boolean registerCompany(UserDTO user, CompanyDTO company) throws Exception;
}

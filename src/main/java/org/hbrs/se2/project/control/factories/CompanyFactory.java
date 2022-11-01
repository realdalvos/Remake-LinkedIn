package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Company;

public class CompanyFactory {

    public static Company createCompany(CompanyDTO companyDTO, UserDTO userDTO) {

        Company company = new Company();

        company.setName(companyDTO.getName());
        company.setUserid(userDTO.getUserid());

        return company;

    }
}

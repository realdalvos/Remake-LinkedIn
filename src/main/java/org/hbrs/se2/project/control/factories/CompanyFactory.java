package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Company;

public class CompanyFactory {

    public static Company createCompany(CompanyDTO companyDTO, UserDTO userDTO) {

        Company company = new Company();
        // pass parameters from companyDTO to company
        company.setName(companyDTO.getName());
        company.setUserid(userDTO.getUserid());
        if(companyDTO.getContactdetails() == null) {
            company.setContactdetails(userDTO.getEmail());
        }else{
            company.setContactdetails(companyDTO.getContactdetails());
        }
        return company;

    }
}

package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.services.CompanyServiceInterface;

public class CompanyService implements CompanyServiceInterface {
    @Override
    public CompanyDTO initialEmailChange(CompanyDTO companyDTO, UserDTO userDTO) {
        if(companyDTO.getContactdetails() == null) {
            companyDTO.setContactdetails(userDTO.getEmail());
        }
        return companyDTO;
    }
}

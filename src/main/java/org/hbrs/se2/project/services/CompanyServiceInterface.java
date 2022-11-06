package org.hbrs.se2.project.services;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;

public interface CompanyServiceInterface {
    CompanyDTO initialEmailChange(CompanyDTO companyDTO, UserDTO userDTO);
}

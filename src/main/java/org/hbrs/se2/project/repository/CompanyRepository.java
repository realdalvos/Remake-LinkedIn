package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    CompanyDTO findByUserid(int id);

    CompanyDTO findByCompanyid(int id);
}

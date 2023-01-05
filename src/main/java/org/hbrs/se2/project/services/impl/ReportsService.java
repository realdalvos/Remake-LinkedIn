package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.ReportsDTO;
import org.hbrs.se2.project.repository.ReportsRepository;
import org.hbrs.se2.project.services.ReportsServiceInterface;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportsService implements ReportsServiceInterface {
    @Autowired
    private EntityCreationService entityCreationService;
    @Autowired
    private ReportsRepository reportsRepository;

    @Override
    public void createReport(ReportsDTO reportsDTO){
    this.reportsRepository.save(entityCreationService.reportsFactory().createEntity(reportsDTO));
    }

    @Override
    public boolean studentHasReportedCompany(int companyid, int studentid){
        return this.reportsRepository.studentHasReportedCompany(companyid, studentid);
    }

    @Override
    public boolean companyShouldBeBanned(CompanyDTO c){
        return reportsRepository.shouldBeBanned(c.getCompanyid());
    }
}

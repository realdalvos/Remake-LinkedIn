package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.ReportsDTO;
import org.hbrs.se2.project.services.impl.ReportsService;
import org.springframework.stereotype.Controller;
@Controller
public class ReportsControl {

    final ReportsService reportsService;

    public ReportsControl(ReportsService reportsService){
        this.reportsService = reportsService;
    }

    /**
     * Save a new report to the Database
     * @param reportsDTO ReportsDTO to be persisted
     */
    public void createReport(ReportsDTO reportsDTO){
        reportsService.createReport(reportsDTO);
    }

    /**
     * Checks if a student has already reported a company
     * @param companyid Company ID
     * @param studentid Student ID
     * @return Boolean indicating if the student has already reported the company
     */
    public boolean studentHasReportedCompany(int companyid, int studentid){
        return  reportsService.studentHasReportedCompany(companyid, studentid);
    }

    /**
     * Checks if the company has more than 4 reports and thus should be banned*/
    public boolean companyShouldBeBanned(CompanyDTO c){
        return reportsService.companyShouldBeBanned(c);
    }
}

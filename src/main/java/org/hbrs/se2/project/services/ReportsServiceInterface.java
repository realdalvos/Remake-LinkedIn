package org.hbrs.se2.project.services;

import org.hbrs.se2.project.dtos.ReportsDTO;

public interface ReportsServiceInterface {
    /**
     * Creates a new Report
     * @param reportsDTO A reports object
     */
    void createReport(ReportsDTO reportsDTO);

    boolean studentHasReportedCompany(int companyid, int studentid);
}

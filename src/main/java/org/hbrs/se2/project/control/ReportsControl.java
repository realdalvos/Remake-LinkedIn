package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.ReportsDTO;
import org.hbrs.se2.project.services.impl.ReportsService;
import org.springframework.stereotype.Controller;
@Controller
public class ReportsControl {

    final ReportsService reportsService;

    public ReportsControl(ReportsService reportsService){
        this.reportsService = reportsService;
    }

    public void createReport(ReportsDTO reportsDTO){
        reportsService.createReport(reportsDTO);
    }
}

package org.hbrs.se2.project.dtos;

import java.io.Serializable;

public interface ReportsDTO extends Serializable {
    int getReportid();

    int getCompanyid();

    String getText();

    int getStudentid();

    void setReportid(int reportid);

    void setCompanyid(int companyid);

    void setText(String text);

    void setStudentid(int studentid);




}

package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.ReportsDTO;
import org.springframework.stereotype.Component;

@Component
public class ReportsDTOImpl implements  ReportsDTO{

    private int reportid;
    private int companyid;
    private String text;
    private int studentid;

    public ReportsDTOImpl(){}

    public ReportsDTOImpl(int reportid, int companyid, String text, int studentid){
        this.reportid = reportid;
        this.companyid = companyid;
        this.text = text;
        this.studentid = studentid;
    }

    @Override
    public int getReportid() {
        return reportid;
    }

    @Override
    public int getCompanyid() {
        return companyid;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getStudentid() {
        return studentid;
    }

    @Override
    public void setReportid(int reportid) {
        this.reportid = reportid;
    }

    @Override
    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }
}

package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.JobDTO;

public class JobDTOImpl implements JobDTO {
    private int jobid;
    private int companyid;
    private String title;
    private String description;
    private String salary;

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public int getJobid() {
        return jobid;
    }

    @Override
    public int getCompanyid() {
        return companyid;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getSalary() {
        return salary;
    }
}

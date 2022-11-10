package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.JobDTO;

import javax.validation.constraints.NotEmpty;

public class JobDTOImpl implements JobDTO {
    private int jobid;
    private int companyid;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String salary;
    @NotEmpty
    private String location;

    public JobDTOImpl() {

    }

    public JobDTOImpl (int companyid) { this.companyid = companyid; }

    public JobDTOImpl(int companyid, String title, String description, String salary, String location) {
        this.companyid = companyid;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.location =location;
    }

    public JobDTOImpl(int jobid, int companyid, String title, String description, String salary, String location) {
        this.jobid = jobid;
        this.companyid = companyid;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.location = location;
    }

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

    @Override
    public String getLocation() { return location; }

    @Override
    public void setLocation(String location) {
        this.location=location;
    }

    @Override
    public String toString() {
        return "JobDTOImpl{" +
                "jobid= " + jobid +
                ", companyid= '" + companyid + '\'' +
                ", title= '" + title + '\'' +
                ", description= '" + description + '\'' +
                ", salary= " + salary + '\'' +
                ", location= " + location +
                '}';
    }
}

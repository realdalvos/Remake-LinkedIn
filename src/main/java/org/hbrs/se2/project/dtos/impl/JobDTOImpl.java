package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.JobDTO;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotEmpty;

@Component
public class JobDTOImpl implements JobDTO {
    private int jobid;
    private int companyid;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    private Integer salary;
    @NotEmpty
    private String location;
    @NotEmpty
    private String contactdetails;

    public JobDTOImpl(){}

    public JobDTOImpl(int companyid) { this.companyid = companyid; }

    public JobDTOImpl(int companyid, String title, String description, Integer salary, String location, String contactdetails) {
        this.companyid = companyid;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.location =location;
        this.contactdetails = contactdetails;
    }

    public JobDTOImpl(int jobid, int companyid, String title, String description, Integer salary, String location, String contactdetails) {
        this.jobid = jobid;
        this.companyid = companyid;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.location = location;
        this.contactdetails = contactdetails;
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

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public int getJobid() {
        return jobid;
    }

    public int getCompanyid() {
        return companyid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSalary() {
        return salary;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) {
        this.location=location;
    }

    public String getContactdetails() { return contactdetails; }

    public void setContactdetails(String contactdetails) { this.contactdetails = contactdetails; }

    @Override
    public String toString() {
        return "JobDTOImpl{" +
                "jobid= " + jobid +
                ", companyid= '" + companyid + '\'' +
                ", title= '" + title + '\'' +
                ", description= '" + description + '\'' +
                ", salary= " + salary + '\'' +
                ", contactdetails= " + contactdetails + '\'' +
                ", location= " + location +
                '}';
    }
}

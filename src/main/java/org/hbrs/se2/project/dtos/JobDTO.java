package org.hbrs.se2.project.dtos;

public interface JobDTO {
    int getJobid();

    void setJobid(int jobid);

    int getCompanyid();

    void setCompanyid(int companyid);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    String getSalary();

    void setSalary(String salary);
}

package org.hbrs.se2.project.dtos;

import java.io.Serializable;

public interface JobDTO extends Serializable {
    int getJobid();

    void setJobid(int jobid);

    int getCompanyid();

    void setCompanyid(int companyid);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Integer getSalary();

    void setSalary(Integer salary);
    String getLocation();
    void setLocation(String location);

    String getContactdetails();

    void setContactdetails(String contactdetails);
}

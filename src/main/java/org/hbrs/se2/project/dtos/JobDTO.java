package org.hbrs.se2.project.dtos;

public interface JobDTO {
    public int getJobid();

    public void setJobid(int jobid);

    public int getCompanyid();

    public void setCompanyid(int companyid);

    public String getTitle();

    public void setTitle(String title);

    public String getDescription();

    public void setDescription(String description);

    public String getSalary();

    public void setSalary(String salary);
}

package org.hbrs.se2.project.dtos;

public interface CompanyDTO {
    public int getCompanyid();

    public void setCompanyid(int id);

    public int getUserid();

    public void setUserid(int userid);

    public String getName();

    public void setName(String name);

    public String getIndustry();

    public void setIndustry(String industry);

    public boolean getBanned();

    public void setBanned(boolean banned);
    public String getContactdetails();
    public String setContactdetails(String contactdetails);
}


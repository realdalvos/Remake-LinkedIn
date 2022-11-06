package org.hbrs.se2.project.dtos;

public interface CompanyDTO {
    int getCompanyid();

    void setCompanyid(int id);

    int getUserid();

    void setUserid(int userid);

    String getName();

    void setName(String name);

    String getIndustry();

    void setIndustry(String industry);

    boolean getBanned();

    void setBanned(boolean banned);
}


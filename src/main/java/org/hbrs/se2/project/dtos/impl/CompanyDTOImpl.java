package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.CompanyDTO;

public class CompanyDTOImpl implements CompanyDTO {
    private int userid;
    private String name;
    private String industry;
    private boolean banned;

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Override
    public int getUserid() {
        return userid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIndustry() {
        return industry;
    }

    @Override
    public boolean getBanned() {
        return banned;
    }
}
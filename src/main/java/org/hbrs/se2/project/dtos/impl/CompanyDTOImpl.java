package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotEmpty;

@Component
public class CompanyDTOImpl implements CompanyDTO {
    private int companyid;
    private int userid;
    @NotEmpty
    private String name;
    private String industry;
    private boolean banned;

    public CompanyDTOImpl(){}

    public CompanyDTOImpl(int userid,String name, String industy, boolean banned){
        this.userid=userid;
        this.name=name;
        this.industry=industy;
        this.banned=banned;
    }
    public void setCompanyid(int id) {
        this.companyid = id;
    }

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

    public int getCompanyid() {return companyid;}
    public int getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public boolean getBanned() {
        return banned;
    }

    @Override
    public String toString() {
        return "JobDTOImpl{" +
                "companyid= " + companyid +
                ", userid= '" + userid + '\'' +
                ", name= '" + name + '\'' +
                ", industry= '" + industry + '\'' +
                ", banned= " + banned +
                '}';
    }

    /**
     * Ich wollte eine equals Methode für das assertEquals in Tests für companyDTOs bauen.
     * Leider funktioniert die nicht. Kann also gelöscht werden, sollten wir die nicht doch noch evtl. brauchen
     * @param companyDTO the companyDTO that is compared
     * @return Are companyDTO and this equal
     */
    public boolean equals(CompanyDTO companyDTO){
        if(this == companyDTO){
            return true;
        } else if (companyDTO == null) {
            return false;
        } else if (companyDTO.getClass() != getClass()) {
            return false;
        } else return this.userid == ((CompanyDTOImpl) companyDTO).userid
                && this.name.equals(((CompanyDTOImpl) companyDTO).name)
                && this.industry.equals(((CompanyDTOImpl) companyDTO).industry)
                && this.banned == ((CompanyDTOImpl) companyDTO).banned
                && this.companyid == ((CompanyDTOImpl) companyDTO).companyid;
    }
}

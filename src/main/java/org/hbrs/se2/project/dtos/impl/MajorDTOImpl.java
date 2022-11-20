package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.MajorDTO;

public class MajorDTOImpl implements MajorDTO {
    private int majorid;
    private String major;
    
    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }


}

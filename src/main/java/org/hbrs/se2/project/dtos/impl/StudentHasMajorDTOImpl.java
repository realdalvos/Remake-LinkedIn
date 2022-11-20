package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.StudentHasMajorDTO;

public class StudentHasMajorDTOImpl implements StudentHasMajorDTO {
    private int studentid;
    private int majorid;

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

}

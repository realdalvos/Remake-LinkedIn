package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.StudentHasSkillDTO;

import javax.persistence.Column;
import javax.persistence.Id;

public class StudentHasSkillDTOImpl implements StudentHasSkillDTO {
    private int studentid;
    private int skillid;

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getSkillid() {
        return skillid;
    }

    public void setSkillid(int skillid) {
        this.skillid= skillid;
    }
}

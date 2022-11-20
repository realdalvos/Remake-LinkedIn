package org.hbrs.se2.project.wrapper;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class StudentHasSkillKey implements Serializable {
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
        this.skillid = skillid;
    }



}

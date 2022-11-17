package org.hbrs.se2.project.entities;

import org.hbrs.se2.project.wrapper.StudentHasSkillKey;

import javax.persistence.*;

@Entity @IdClass(StudentHasSkillKey.class)
@Table(name = "student_has_skill", schema = "mid9db")
public class StudentHasSkill {

    private int studentid;
    private int skillid;

    @Id
    @Column(name = "studentid")
    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    @Id
    @Column(name = "skillid")
    public int getSkillid() {
        return skillid;
    }

    public void setSkillid(int skillid) {
        this.skillid = skillid;
    }


}

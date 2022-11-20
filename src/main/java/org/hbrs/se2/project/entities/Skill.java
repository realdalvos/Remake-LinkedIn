package org.hbrs.se2.project.entities;

import javax.persistence.*;
@Entity
@Table(name = "skill", schema = "mid9db")
public class Skill {
    private int skillid;
    private String skill;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "skillid")
    public int getSkillid() {
        return skillid;
    }

    public void setSkillid(int skillid) {
        this.skillid = skillid;
    }

    @Basic
    @Column(name = "skill")
    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

}

package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.SkillDTO;

public class SkillDTOImpl implements SkillDTO {
    private int skillid;
    private String skill;

    public int getSkillid() {
        return skillid;
    }

    public void setSkillid(int skillid) {
        this.skillid = skillid;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

}

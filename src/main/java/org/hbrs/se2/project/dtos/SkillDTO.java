package org.hbrs.se2.project.dtos;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public interface SkillDTO {
    public int getSkillid();
    public void setSkillid(int skillid);
    public String getSkill();
    public void setSkill(String skill);
}

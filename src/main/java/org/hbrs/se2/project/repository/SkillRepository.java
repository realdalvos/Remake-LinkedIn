package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    SkillDTO findBySkill(String skill);

    SkillDTO findBySkillid(int skillid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Skill s WHERE s.skillid=:skillid")
    void deleteBySkillid(int skillid);

}

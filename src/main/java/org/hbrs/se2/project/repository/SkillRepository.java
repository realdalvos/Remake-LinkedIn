package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    SkillDTO findBySkill(String skill);

    SkillDTO findBySkillid(int skillid);

    @Query("SELECT s FROM Skill s WHERE LOWER(s.skill) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SkillDTO> findByKeyword(String keyword);

}

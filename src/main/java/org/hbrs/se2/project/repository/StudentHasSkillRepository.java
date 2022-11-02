package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.entities.StudentHasSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface StudentHasSkillRepository extends JpaRepository<StudentHasSkill, Integer> {
}

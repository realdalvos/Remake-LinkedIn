package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.entities.Job;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository  extends JpaRepository<Profile, Integer> {
}

package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    UserDTO findByUserid(int id);

    UserDTO findByUsername(String username);

    UserDTO findByEmail(String email);
}
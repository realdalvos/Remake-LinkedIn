package org.hbrs.se2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.User;

@Component
/**
 * https://www.baeldung.com/spring-data-jpa-projections
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 *
 * Additional links from gwolni2s
 * https://www.geeksforgeeks.org/spring-boot-difference-between-crudrepository-and-jparepository/#:~:text=JpaRepository%20is%20a%20JPA%20(Java,API%20for%20pagination%20and%20sorting.
 * https://spring.io/guides/gs/accessing-data-mysql/
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    UserDTO findUserByUseridAndPassword(int id, String password);

    UserDTO findUserByUsernameAndPassword(String username, String password);

    UserDTO findUserByUsername(String username);
}

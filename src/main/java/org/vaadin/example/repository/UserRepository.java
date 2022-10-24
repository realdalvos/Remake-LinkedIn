package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.vaadin.example.dtos.UserDTO;
import org.vaadin.example.entities.User;

@Component
/**
 * JPA-Repository für die Abfrage von registrierten User. Die Bezeichnung einer Methode
 * bestimmt dabei die Selektionsbedingung (den WHERE-Teil). Der Rückgabewert einer
 * Methode bestimmt den Projectionsbedingung (den SELECT-Teil).
 * Mehr Information über die Entwicklung von Queries in JPA:
 * https://www.baeldung.com/spring-data-jpa-projections
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 *
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    UserDTO findUserByUseridAndPassword(int id, String password);

    UserDTO findUserByUsernameAndPassword(String username, String password);
}

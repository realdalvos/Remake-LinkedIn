package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * https://www.baeldung.com/spring-data-jpa-projections
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 *
 * Additional links from gwolni2s
 * https://www.geeksforgeeks.org/spring-boot-difference-between-crudrepository-and-jparepository/#:~:text=JpaRepository%20is%20a%20JPA%20(Java,API%20for%20pagination%20and%20sorting.
 * https://spring.io/guides/gs/accessing-data-mysql/
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    StudentDTO findStudentByUserid(int id);

    StudentDTO findStudentByMatrikelnumber(String matrikelnumber);

    @Transactional
    @Query("SELECT s FROM Student s")
    List<StudentDTO> getAll();
}



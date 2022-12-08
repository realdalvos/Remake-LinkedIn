package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    StudentDTO findByMatrikelnumber(String matrikelnumber);

    StudentDTO findByUserid(int id);

    @Query("SELECT s FROM Student s")
    List<StudentDTO> getAll();

    @Query("SELECT s FROM Student s WHERE LOWER(s.university) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<StudentDTO> findByKeyword(String keyword);

}

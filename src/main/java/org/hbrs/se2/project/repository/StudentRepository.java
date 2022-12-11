package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    StudentDTO findByMatrikelnumber(String matrikelnumber);

    StudentDTO findByUserid(int id);

    StudentDTO findByStudentid(int id);

    @Transactional
    @Query("SELECT s FROM Student s")
    List<StudentDTO> getAll();

}

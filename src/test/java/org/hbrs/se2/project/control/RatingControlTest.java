package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.RatingDTOImpl;
import org.hbrs.se2.project.repository.RatingRepository;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RatingControlTest {

    @Autowired
    RatingControl ratingControl;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    HelperForTests h;
    @Autowired
    CompanyDTO companyDTO;

    List<StudentDTO> students;

    @BeforeEach
    void setUp() {
        students = h.registerTestStudents(4);
        companyDTO = h.registerTestCompany();
    }

    @AfterEach
    @DisplayName("Deleting the test users. Deleting a company will also delete its ratings. Ratings from deleted students will remain in the Database.")
    void tearDown() {
        h.deleteTestUsers();
    }

    @Test
    @DisplayName("Tests the correct creation of ratings.")
    void createRatingTest(){
        RatingDTO rating1 = new RatingDTOImpl(students.get(0).getStudentid(), companyDTO.getCompanyid(), 3);
        RatingDTO rating2 = new RatingDTOImpl(students.get(1).getStudentid(), companyDTO.getCompanyid(), 4);

        ratingControl.createRating(rating1);
        ratingControl.createRating(rating2);

        assertEquals(3, ratingRepository.findByStudentid(students.get(0).getStudentid()).getRating());
        assertEquals(4, ratingRepository.findByStudentid(students.get(1).getStudentid()).getRating());
    }

    @Test
    @DisplayName("Tests if ratings are retrieved properly calculated from the Database.")
    void getRatingTest(){
        RatingDTO rating1 = new RatingDTOImpl(students.get(0).getStudentid(), companyDTO.getCompanyid(), 3);
        RatingDTO rating2 = new RatingDTOImpl(students.get(1).getStudentid(), companyDTO.getCompanyid(), 4);

        ratingControl.createRating(rating1);
        ratingControl.createRating(rating2);

        assertEquals(3.5F, ratingControl.getRating(companyDTO.getCompanyid()));

        RatingDTO rating3 = new RatingDTOImpl(students.get(2).getStudentid(), companyDTO.getCompanyid(), 1);
        RatingDTO rating4 = new RatingDTOImpl(students.get(3).getStudentid(), companyDTO.getCompanyid(), 3);

        ratingControl.createRating(rating3);
        ratingControl.createRating(rating4);

        assertEquals(2.75F, ratingControl.getRating(companyDTO.getCompanyid()));
    }

    @Test
    @DisplayName("Tests if the check if a student has already rated a company or not works properly.")
    void studentHasRatedCompanyTest(){
        RatingDTO rating1 = new RatingDTOImpl(students.get(0).getStudentid(), companyDTO.getCompanyid(), 3);

        ratingControl.createRating(rating1);

        assertTrue(ratingControl.studentHasRatedCompany(companyDTO.getCompanyid(), students.get(0).getStudentid()));
        assertFalse(ratingControl.studentHasRatedCompany(companyDTO.getCompanyid(), students.get(1).getStudentid()));
    }

}

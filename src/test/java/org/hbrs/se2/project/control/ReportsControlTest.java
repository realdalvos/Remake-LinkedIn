package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.ReportsDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.dtos.impl.ReportsDTOImpl;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.repository.ReportsRepository;
import org.hbrs.se2.project.services.impl.ReportsService;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportsControlTest {

    @Autowired
    HelperForTests helper;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobControl jobControl;
    @Autowired
    ReportsControl reportsControl;
    @Autowired
    JobDTO testJob;
    @Autowired
    CompanyDTO testCompanyDTO;
    @Autowired
    UserControl userControl;
    @Autowired
    StudentDTO testStudent;
    @Autowired
    ReportsRepository reportsRepository;

    List<StudentDTO> registeredStudents = new ArrayList<>();


    @BeforeEach
    void setUp(){
        testCompanyDTO = helper.registerTestCompany();
        testJob = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title", "Testbeschreibung. assembly programmer.", 20, "Test location", "Test contactdetails");
        jobControl.createNewJobPost(testJob);
        testJob = jobRepository.findByCompanyidAndTitle(testJob.getCompanyid(), testJob.getTitle());
        registeredStudents = helper.registerTestStudents(5);
        testStudent = registeredStudents.get(0);
    }

    @AfterEach
    void tearDown(){
        helper.deleteTestUsers();
    }

    @Test
    @DisplayName("Tests if a report is created and that the report exists")
    void reportTest(){
        reportsControl.createReport(new ReportsDTOImpl(testJob.getCompanyid(), testStudent.getStudentid()));
        assertTrue(reportsControl.studentHasReportedCompany(testCompanyDTO.getCompanyid(), testStudent.getStudentid()));
    }

    @Test
    @DisplayName("Tests if the company should be banned")
    void bannableTest(){
        reportsControl.createReport(new ReportsDTOImpl(testJob.getCompanyid(), testStudent.getStudentid()));
        assertFalse(reportsRepository.shouldBeBanned(testJob.getCompanyid()));
        reportsControl.createReport(new ReportsDTOImpl(testJob.getCompanyid(), registeredStudents.get(1).getStudentid()));
        reportsControl.createReport(new ReportsDTOImpl(testJob.getCompanyid(), registeredStudents.get(2).getStudentid()));
        reportsControl.createReport(new ReportsDTOImpl(testJob.getCompanyid(), registeredStudents.get(3).getStudentid()));
        reportsControl.createReport(new ReportsDTOImpl(testJob.getCompanyid(), registeredStudents.get(4).getStudentid()));
        assertTrue(reportsRepository.shouldBeBanned(testJob.getCompanyid()));
    }
}

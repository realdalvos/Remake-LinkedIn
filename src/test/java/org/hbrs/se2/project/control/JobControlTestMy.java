package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.repository.UserRepository;
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
class JobControlTestMy {
    @Autowired
    RegistrationControl registrationControl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JobControl jobControl = new JobControl();
    JobDTOImpl testJob;

    @BeforeEach
    void setUp() {
        // create new jobDTOImpl
        testJob = new JobDTOImpl(
                0, "Test title", "Das hier ist eine Testbeschreibung. Wir suchen einen assembly programmer.", "20 Euro");
        jobControl.createNewJobPost(testJob);
    }

    @AfterEach
    void tearDown() {
        //remove job
    }

    @Test
    @DisplayName("")
    void getJobsMatchingKeyword() {
        List<JobDTOImpl> list = jobControl.getJobsMatchingKeyword("Test");
        assertTrue(containsTestJob(list));

        //search for keyword in title
        list = jobControl.getJobsMatchingKeyword("title");
        assertTrue(containsTestJob(list));
        //search for keyword in title in Uppercase
        list = jobControl.getJobsMatchingKeyword("Title");
        assertTrue(containsTestJob(list));
        //search for keyword in description
        list = jobControl.getJobsMatchingKeyword("assembly");
        assertTrue(containsTestJob(list));
        //search for keyword in description with a following "."
        list = jobControl.getJobsMatchingKeyword("programmer");
        assertTrue(containsTestJob(list));
        //search for missing keyword
        list = jobControl.getJobsMatchingKeyword("java");
        assertFalse(containsTestJob(list));
    }

    @Test
    @DisplayName("")
    void getAllJobsData() {
        List<JobDTOImpl> tmp = new ArrayList<JobDTOImpl>();
        tmp.add(testJob);

        jobControl.getAllJobsData(tmp);
    }

    private boolean containsTestJob(List<JobDTOImpl> list){
        boolean foundTestJob = false;
        for(JobDTOImpl job : list){
            if(job.getTitle().equals(testJob.getTitle())){
                foundTestJob = true;
            }
        }
        return foundTestJob;
    }
}
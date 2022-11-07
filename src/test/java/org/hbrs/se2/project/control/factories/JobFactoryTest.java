package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.entities.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobFactoryTest {

    JobDTO testJob;

    @BeforeEach
    void setUp() {
        JobDTOImpl testJobImpl = new JobDTOImpl(
                99999, "Test title", "Testbeschreibung. assembly programmer.", "20 Euro","Testlocation");
        testJobImpl.setJobid(1);

        testJob = testJobImpl;

    }

    @Test
    void createJob() {
        Job jobFromFactory = JobFactory.createJob(testJob);
        assertNotNull(jobFromFactory, "createJob Method should return a Instance of Job and not null.");

        //Checking the values of the returned job
        assertEquals(testJob.getCompanyid(), jobFromFactory.getCompanyid(), "CompanyID should match.");
        assertEquals(testJob.getJobid(), jobFromFactory.getJobid(), "JobID should match.");
        assertEquals(testJob.getSalary(), jobFromFactory.getSalary(), "Salary should match.");
        assertEquals(testJob.getTitle(), jobFromFactory.getTitle(), "Title should match.");
        assertEquals(testJob.getDescription(), jobFromFactory.getDescription(), "Description should match.");
    }
}
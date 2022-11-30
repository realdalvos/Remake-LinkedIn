package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JobControlTest {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobControl jobControl;
    //Test job
    @Autowired
    JobDTO testJob;
    @Autowired
    CompanyDTO testCompanyDTO;
    @Autowired
    HelperForTests h;

    @BeforeEach
    void setUp() {
        testCompanyDTO = h.registerTestCompany();

        // create and save a new job
        testJob = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title", "Testbeschreibung. assembly programmer.", 20, "Test location", "Test contactdetails");
        jobControl.createNewJobPost(testJob);

        testJob = jobRepository.findByCompanyidAndTitle(testJob.getCompanyid(), testJob.getTitle());
    }

    @AfterEach
    @DisplayName("Deleting user \"JUnitTest\". By deleting a user all jobs issued by them get deleted as well.")
    void tearDown(){
        h.deleteTestUsers();
    }

    @Test
    @DisplayName("Tests if the test Job was created in the database.")
    public void testIfJobIsCreatedInDB(){
        //Checking if there is a job with companyid of the testCompany and title of the testJob
        JobDTO jobFromRepo = jobRepository.findByCompanyidAndTitle(testJob.getCompanyid(), testJob.getTitle());
        Assertions.assertNotNull(jobFromRepo);

        assertEquals("Testbeschreibung. assembly programmer.", jobFromRepo.getDescription());
        assertEquals(20, jobFromRepo.getSalary());
    }

    @Test
    @DisplayName("Tests if the getJobsMatchingKeyword Method works as expected.")
    void getJobsMatchingKeyword() {
        List<JobDTO> list = jobControl.getJobsMatchingKeyword("Test");
        assertTrue(containsTestJob(list));

        //search for exact keyword in title
        list = jobControl.getJobsMatchingKeyword("title");
        assertTrue(containsTestJob(list), "Test job should be one of the results.");
        //search for keyword in title in Uppercase
        list = jobControl.getJobsMatchingKeyword("Title");
        assertTrue(containsTestJob(list), "Test job should be one of the results.");
        //search for keyword in description
        list = jobControl.getJobsMatchingKeyword("assembly");
        assertTrue(containsTestJob(list), "Test job should be one of the results.");
        //search for keyword in description with a following "."
        list = jobControl.getJobsMatchingKeyword("programmer");
        assertTrue(containsTestJob(list), "Test job should be one of the results.");
        //search for missing keyword
        list = jobControl.getJobsMatchingKeyword("java");
        assertFalse(containsTestJob(list), "Test job should NOT be one of the results.");


        //Add another job
        // create and save another new job
        JobDTOImpl secondJob = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Not matching", "Some description. assembly programmer.", 20, "Test location", "Test contactdetails");
        jobControl.createNewJobPost(secondJob);

        //should return both jobs since both contain the keyword "assembly"
        list = jobControl.getJobsMatchingKeyword("assembly");
        assertTrue(containsTestJob(list), "First job should be returned");
        assertTrue(containsJob(list, secondJob), "Second job should be returned");

        //should return only first job since only the first job contains the keyword "test"
        list = jobControl.getJobsMatchingKeyword("test");
        assertTrue(containsTestJob(list), "Only first job should be returned");
    }

    /**
     * Checks if the job testJob is contained in the list.*/
    private boolean containsTestJob(List<JobDTO> list){
        return containsJob(list, testJob);
    }

    /**
     * Checks if any given job is contained in the list.*/
    private boolean containsJob(List<JobDTO> list, JobDTO relevantJob){
        boolean foundTestJob = false;
        /*
        since only the jobid generated in the database is unique, in theorie there could be 2 jobs with matching companyid, title, description and salary, lets call those 2 jobs jobA and jobB.
        If we want to test that jobB gets returned we have te following problem(?): You cant be sure if the returned job is jobA or jobB since you dont know the jobID before putting these jobs in the database.
        Not sure right now if that will become a problem later on, just wanted to point it out.
         */
        for(JobDTO job : list){
            if(relevantJob.getCompanyid() == job.getCompanyid() && relevantJob.getTitle().equals(job.getTitle()) && relevantJob.getDescription().equals(job.getDescription()) && relevantJob.getSalary().equals(job.getSalary())){
                foundTestJob = true;
            }
        }
        return foundTestJob;
    }

    @Test
    @DisplayName("Tests if getCompanyOfJob returns the correct result")
    void test_getCompanyOfJob() {
        assertEquals(testCompanyDTO.getName(), jobControl.getCompanyOfJob(testJob), "Company name does not match");
    }

    @Test
    @DisplayName("Testing if getAllCompanyJobs works as expected.")
    void test_getAllCompanyJobs(){
        //2 companies are needed for testing
        List<CompanyDTO> listOfCompanies = h.registerTestCompany(2);
        testCompanyDTO = listOfCompanies.get(0);
        CompanyDTO testCompanyDTO2 = listOfCompanies.get(1);

        //Update companyid of testJob since a new company was created
        testJob = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title", "Testbeschreibung. assembly programmer.", 20, "Test location", "Test contactdetails");
        jobControl.createNewJobPost(testJob);

        List<JobDTO> list = jobControl.getAllCompanyJobs(testCompanyDTO.getCompanyid());

        //Should return list containing only test job
        assertEquals(1, list.size(), "List should contain 1 element");
        assertTrue(containsJob(list, testJob), "Should contain the test job");

        JobDTO testJob2 = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title2", "Testbeschreibung2", 40, "Test location2", "Test contactdetails2");

        jobControl.createNewJobPost(testJob2);
        list = jobControl.getAllCompanyJobs(testCompanyDTO.getCompanyid());

        //Should return list containing only testJob and testJob2
        assertEquals(2, list.size(), "List should contain 2 elements.");
        assertTrue(containsJob(list, testJob), "Should contain the testJob");
        assertTrue(containsJob(list, testJob2), "Should contain testJob2");


        JobDTO testJob3 = new JobDTOImpl(
                testCompanyDTO2.getCompanyid(), "Test title3", "Testbeschreibung3", 40, "Test location3", "Test contactdetails3");

        jobControl.createNewJobPost(testJob3);
        list = jobControl.getAllCompanyJobs(testCompanyDTO.getCompanyid());

        //Should return list containing only testJob and testJob2, NOT testJob3
        assertEquals(2, list.size(), "List should contain 2 elements.");
        assertTrue(containsJob(list, testJob), "Should contain the testJob");
        assertTrue(containsJob(list, testJob2), "Should contain testJob2");
        assertFalse(containsJob(list, testJob3), "Should NOT contain testJob3 since it the companyid differs");

        list = jobControl.getAllCompanyJobs(testCompanyDTO2.getCompanyid());

        //Should return list containing only testJob3, NOT testJob2 or testJob3
        assertEquals(1, list.size(), "List should contain 1 element.");
        assertFalse(containsJob(list, testJob), "Should NOT contain the testJob since it the companyid differs");
        assertFalse(containsJob(list, testJob2), "Should NOT contain testJob2 since it the companyid differs");
        assertTrue(containsJob(list, testJob3), "Should contain testJob3.");
    }

    @Test
    @DisplayName("Testing the deleteJob Method")
    void test_removeJob() {
        JobDTO testJob2 = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title2", "Testbeschreibung. assembly programmer.", 20, "Test location", "Test contactdetails");
        jobControl.createNewJobPost(testJob2);
        testJob2 = jobRepository.findByCompanyidAndTitle(testJob2.getCompanyid(), testJob2.getTitle());

        List<JobDTO> list = jobControl.getAllCompanyJobs(testCompanyDTO.getCompanyid());
        assertEquals(2, list.size(), "List should contain 2 elements");

        jobControl.deleteJob(testJob2.getJobid());

        list = jobControl.getAllCompanyJobs(testCompanyDTO.getCompanyid());
        assertEquals(1, list.size(), "List should contain 1 element");
        assertNotNull(jobRepository.findByCompanyidAndTitle(testJob.getCompanyid(), testJob.getTitle()), "testJob should be in DB");

        jobControl.deleteJob(testJob.getJobid());
        list = jobControl.getAllCompanyJobs(testCompanyDTO.getCompanyid());
        assertEquals(0, list.size(), "List should contain 0 elements");

        int jobID = testJob.getJobid();
        EmptyResultDataAccessException thrown = assertThrows(EmptyResultDataAccessException.class, () -> jobControl.deleteJob(testJob.getJobid()));
        assertEquals("No class org.hbrs.se2.project.entities.Job entity with id " + jobID + " exists!", thrown.getMessage());
    }

    @Test
    @DisplayName("Testing the getCompanyByUserid Method")
    void test_getCompanyByUserid() {
        CompanyDTO companyByUserid = jobControl.getCompanyByUserid(testCompanyDTO.getUserid());

        assertNotNull(companyByUserid, "Should return the test company");

        assertEquals(testCompanyDTO.getCompanyid(), companyByUserid.getCompanyid(), "Should have the same companyid since its the same company.");
    }
}

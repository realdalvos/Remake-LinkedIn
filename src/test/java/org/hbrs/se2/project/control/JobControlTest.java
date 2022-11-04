package org.hbrs.se2.project.control;


import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.hbrs.se2.project.control.factories.CompanyFactory.createCompany;
import static org.hbrs.se2.project.control.factories.UserFactory.createUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JobControlTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobControl jobControl = new JobControl();
    JobDTO testJob;
    CompanyDTO testCompanyDTO;


    @BeforeEach
    void setUp() {
        deleteTestUser();

        //Creating and saving test user
        UserDTOImpl testUserDTOImpl = new UserDTOImpl("JUnitTest","Test","Test@mail.com","Test");
        userRepository.save(createUser(testUserDTOImpl));
        UserDTO testUserDTO = userRepository.findUserByUsername("JUnitTest");

        //creating and saving test company
        CompanyDTOImpl testCompanyDTOImpl = new CompanyDTOImpl(testUserDTO.getUserid(),"TestCompany","Test",false);
        companyRepository.save(createCompany(testCompanyDTOImpl,testUserDTO));
        testCompanyDTO = companyRepository.findCompanyByUserid(testCompanyDTOImpl.getUserid());

        // create and save another new job
        testJob = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title", "Testbeschreibung. assembly programmer.", "20 Euro");
        jobControl.createNewJobPost(testJob);

        testJob = jobRepository.findJobByCompanyidAndTitle(testJob.getCompanyid(), testJob.getTitle());
    }

    @AfterEach
    @DisplayName("Deleting user \"JUnitTest\". By deleting a user all jobs issued by them get deleted as well.")
    void tearDown(){
        deleteTestUser();
    }

    /**
     * Delete user "JUnitTest".*/
    private void deleteTestUser(){
        UserDTO user = userRepository.findUserByUsername("JUnitTest");
        if(user != null) {
            userRepository.deleteById(user.getUserid());
        }
    }

    @Test
    public void testIfJobIsCreatedInDB(){
        //Checking if there is a job with companyid of the testCompany and title of the testJob
        JobDTO jobFromRepo = jobRepository.findJobByCompanyidAndTitle(testJob.getCompanyid(), testJob.getTitle());
        Assertions.assertNotNull(jobFromRepo);

        assertEquals("Testbeschreibung. assembly programmer.", jobFromRepo.getDescription());
        assertEquals("20 Euro", jobFromRepo.getSalary());
    }

    @Test
    @DisplayName("Tests if the getJobsMatchingKeyword Method works as expected.")
    void getJobsMatchingKeyword() {
        List<JobDTOImpl> list = jobControl.getJobsMatchingKeyword("Test");
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
        // create and save new job
        JobDTOImpl secondJob = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Not matching", "Some description. assembly programmer.", "20 Euro");
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
    private boolean containsTestJob(List<JobDTOImpl> list){
        boolean foundTestJob = false;
        for(JobDTOImpl job : list){
            //If jobid matches than its definitely the testjob
            if(job.getJobid() == testJob.getJobid()){
                foundTestJob = true;
            }
        }
        return foundTestJob;
    }

    /**
     * Checks if any given job is contained in the list.*/
    private boolean containsJob(List<JobDTOImpl> list, JobDTO relevantJob){
        boolean foundTestJob = false;
        /*
        since only the jobid generated in the database is unique, in theorie there could be 2 jobs with matching companyid, title, description and salary, lets call those 2 jobs jobA and jobB.
        If we want to test that jobB gets returned we have te following problem(?): You cant be sure if the returned job is jobA or jobB since you dont know the jobID before putting these jobs in the database.
        Not sure right now if that will become a problem later on, just wanted to point it out.
         */
        for(JobDTOImpl job : list){
            if(relevantJob.getCompanyid() == job.getCompanyid() && relevantJob.getTitle().equals(job.getTitle()) && relevantJob.getDescription().equals(job.getDescription()) && relevantJob.getSalary().equals(job.getSalary())){
                foundTestJob = true;
            }
        }
        return foundTestJob;
    }

    @Test
    @DisplayName("Tests if getAllJobsData produces correct results.")
    void getAllJobsData() {
        JobDTOImpl testJobImpl = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title", "Testbeschreibung. assembly programmer.", "20 Euro");

        List<JobDTOImpl> tmp = new ArrayList<>();
        tmp.add(testJobImpl);

        JobsView.JobDetail jobDetail = jobControl.getAllJobsData(tmp).get(0);

        assertEquals("Testbeschreibung. assembly programmer.", jobDetail.getDescription());
        assertEquals("Test title", jobDetail.getTitle());
        assertEquals("20 Euro", jobDetail.getSalary());
        assertEquals("Test@mail.com", jobDetail.getEmail());
        assertEquals("TestCompany", jobDetail.getName());
    }
}

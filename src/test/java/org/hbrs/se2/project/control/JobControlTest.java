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
    UserRepository testUserRepository;
    @Autowired
    CompanyRepository testCompanyRepository;
    @Autowired
    JobRepository testJobRepository;
    @Autowired
    JobControl jobControl = new JobControl();
    JobDTOImpl testJob;

    @BeforeEach
    void setUp() {
        //Creating and saving test user
        UserDTOImpl testUserDTOImpl = new UserDTOImpl("Test","Test","Test@mail.com","Test");
        testUserRepository.save(createUser(testUserDTOImpl));
        UserDTO testUserDTO =testUserRepository.findUserByUsername("Test");

        //creating and saving test company
        CompanyDTOImpl testCompanyDTOImpl = new CompanyDTOImpl(testUserDTO.getUserid(),"TestCompany","Test",false);
        testCompanyRepository.save(createCompany(testCompanyDTOImpl,testUserDTO));
        CompanyDTO testCompanyDTO = testCompanyRepository.findCompanyByUserid(testCompanyDTOImpl.getUserid());

        /*
        //creating and saving new job
        JobDTO testJobDTO = new JobDTOImpl(
                testCompanyDTO.getCompanyid(),"99999","Test","Test");
        jobControl.createNewJobPost(testJobDTO);
        */

        // create and saving another new job
        testJob = new JobDTOImpl(
                testCompanyDTO.getCompanyid(), "Test title", "Testbeschreibung. assembly programmer.", "20 Euro");
        jobControl.createNewJobPost(testJob);
    }

    @AfterEach
    void tearDown() {
        //remove job
        //remove User
    }

    @Test
    public void testIfJobIsCreatedInDB(){
        //Checking if there is a job with companyid of the testCompany and title of the testJob
        JobDTO jobFromRepo = testJobRepository.findJobByCompanyidAndTitle(testJob.getCompanyid(), testJob.getTitle());
        Assertions.assertNotNull(jobFromRepo);

        assertEquals("Testbeschreibung. assembly programmer.", jobFromRepo.getDescription());
        assertEquals("20 Euro", jobFromRepo.getSalary());
    }

    @Test
    @DisplayName("Tests if the getJobsMatchingKeyword Method works as expected.")
    void getJobsMatchingKeyword() {
        List<JobDTOImpl> list = jobControl.getJobsMatchingKeyword("Test");
        assertTrue(containsTestJob(list));

        //search for keyword in title
        list = jobControl.getJobsMatchingKeyword("title");
        assertTrue(containsTestJob(list), "Test Job should be one of the results.");
        //search for keyword in title in Uppercase
        list = jobControl.getJobsMatchingKeyword("Title");
        assertTrue(containsTestJob(list), "Test Job should be one of the results.");
        //search for keyword in description
        list = jobControl.getJobsMatchingKeyword("assembly");
        assertTrue(containsTestJob(list), "Test Job should be one of the results.");
        //search for keyword in description with a following "."
        list = jobControl.getJobsMatchingKeyword("programmer");
        assertTrue(containsTestJob(list), "Test Job should be one of the results.");
        //search for missing keyword
        list = jobControl.getJobsMatchingKeyword("java");
        assertFalse(containsTestJob(list), "Test Job should NOT be one of the results.");
    }

    @Test
    @DisplayName("")
    void getAllJobsData() {
        List<JobDTOImpl> tmp = new ArrayList<JobDTOImpl>();
        tmp.add(testJob);

        JobsView.JobDetail jobDetail = jobControl.getAllJobsData(tmp).get(0);

        assertEquals("Testbeschreibung. assembly programmer.", jobDetail.getDescription());
        assertEquals("Test title", jobDetail.getTitle());
        assertEquals("20 Euro", jobDetail.getSalary());
        assertEquals("Test@mail.com", jobDetail.getEmail());
        assertEquals("TestCompany", jobDetail.getName());
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

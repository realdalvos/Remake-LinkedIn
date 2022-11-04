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
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hbrs.se2.project.control.factories.CompanyFactory.createCompany;
import static org.hbrs.se2.project.control.factories.UserFactory.createUser;

@SpringBootTest
public class JobControlTest {
    @Autowired
    UserRepository testUserRepository;
    @Autowired
    CompanyRepository testCompanyRepository;
    @Autowired
    JobRepository testJobRepository;
    @Autowired
    JobControl testJobControl = new JobControl();
    @Test
    public void testIfJobIsCreatedInDB(){
        UserDTOImpl testUserDTOImpl = new UserDTOImpl("Test","Test","Test","Test");
        testUserRepository.save(createUser(testUserDTOImpl));
        UserDTO testUserDTO =testUserRepository.findUserByUsername("Test");
        testUserDTOImpl.setUserid(testUserDTO.getUserid());

        CompanyDTOImpl testCompanyDTOImpl = new CompanyDTOImpl(testUserDTOImpl.getUserid(),"Test","Test",false);
        testCompanyRepository.save(createCompany(testCompanyDTOImpl,testUserDTOImpl));
        CompanyDTO testCompanyDTO = testCompanyRepository.findCompanyByUserid(testCompanyDTOImpl.getUserid());
        testCompanyDTOImpl.setCompanyid(testCompanyDTO.getCompanyid());

        JobDTOImpl testJobDTOImpl = new JobDTOImpl(testCompanyDTOImpl.getCompanyid(),"99999","Test","Test");
        testJobControl.createNewJobPost(testJobDTOImpl);

        Assertions.assertNotNull(testJobRepository.findJobByCompanyidAndTitle(testJobDTOImpl.getCompanyid(),testJobDTOImpl.getTitle()));
        if(testJobRepository.findJobByCompanyidAndTitle(testJobDTOImpl.getCompanyid(),testJobDTOImpl.getTitle()) != null) {
            JobDTO testJobDTO = testJobRepository.findJobByCompanyidAndTitle(testJobDTOImpl.getCompanyid(), testJobDTOImpl.getTitle());
            Assertions.assertEquals("Test",testJobDTO.getDescription());
            Assertions.assertEquals("Test",testJobDTO.getSalary());
        }
        testUserRepository.deleteById(testUserDTOImpl.getUserid());
    }
}

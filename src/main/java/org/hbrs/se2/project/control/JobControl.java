package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.factories.JobFactory;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.entities.Job;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class JobControl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;
    
    public CompanyDTO getCompanyByUserid(int id) {
        return companyRepository.findCompanyByUserid(id);
    }

    /**
     * Saving a new job to the database.*/
    public void createNewJobPost(JobDTO job) {
        this.jobRepository.save(JobFactory.createJob(job));
    }

    /**
     * @param keyword The word to search for.
     * @return List of jobs that contain the keyword in either the job title or the job description.*/
    public List<JobDTOImpl> getJobsMatchingKeyword(String keyword) {
        // empty list jobDTOImpls
        List<JobDTOImpl> jobDTOs = new ArrayList<>();
        // get all jobs
        List<Job> jobs = jobRepository.findAll();
        // transform all job entities into jobDTOs
        JobDTOImpl jobDTO;
        for(Job job : jobs) {
            jobDTO = new JobDTOImpl(
                job.getJobid(), job.getCompanyid(), job.getTitle(), job.getDescription(), job.getSalary()
            );
            jobDTOs.add(jobDTO);
        }
        return getFilteredJobs(jobDTOs, keyword);
    }

    /**
     * Search for the given keyword in either the job title or the job description
     * of the given jobs and return the matching jobs.
     * @param jobs List of all jobs.
     * @param keyword The word to search for.
     * @return List of matching jobs.*/
    private List<JobDTOImpl> getFilteredJobs(List<JobDTOImpl> jobs, String keyword) {
        List<JobDTOImpl> matchingJobs = new ArrayList<>();
        for(JobDTOImpl job : jobs) {
            if(job.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            } else if(job.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            }
        }
        return matchingJobs;
    }

    /**Makes a List of JobsView.JobDetail from the given List of JobDTOImpl
     * @param jobs List of JobDTOImpl.
     * @return List of JobsView.JobDetail*/
    public List<JobsView.JobDetail> getAllJobsData(List<JobDTOImpl> jobs) {
        //empty list
        List<JobsView.JobDetail> jobsData = new ArrayList<>();
        CompanyDTO companyDTO;
        UserDTO userDTO;


        for(JobDTOImpl job : jobs) {
            //get CompanyDTO from company database with company id given in job
            companyDTO = companyRepository.findCompanyByCompanyid(job.getCompanyid());
            //get UserDTO from user database with user id given in companyDTO
            userDTO = userRepository.findUserByUserid(companyDTO.getUserid());
            //create new JobDetail and add it to jobsData
            jobsData.add(new JobsView.JobDetail(job.getTitle(), job.getSalary(), job.getDescription(), companyDTO.getName(), userDTO.getEmail()));
        }
        return jobsData;
    }
}


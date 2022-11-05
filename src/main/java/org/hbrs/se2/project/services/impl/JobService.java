package org.hbrs.se2.project.services.impl;

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
import org.hbrs.se2.project.services.JobServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService implements JobServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public CompanyDTO getCompanyByUserid(int id) {
        return companyRepository.findCompanyByUserid(id);
    }

    @Override
    public void createNewJobPost(JobDTO job) {
        this.jobRepository.save(JobFactory.createJob(job));
    }

    @Override
    public List<JobDTO> getJobsMatchingKeyword(String keyword) {
        // empty list jobDTOs
        List<JobDTO> jobDTOs = new ArrayList<>();
        // get all jobs
        List<Job> jobs = jobRepository.findAll();
        // transform all job entities into jobDTOs
        JobDTO jobDTO;
        for(Job job : jobs) {
            jobDTO = new JobDTOImpl(
                    job.getJobid(), job.getCompanyid(), job.getTitle(), job.getDescription(), job.getSalary()
            );
            jobDTOs.add(jobDTO);
        }
        return getFilteredJobs(jobDTOs, keyword);
    }

    @Override
    public List<JobsView.JobDetail> getAllJobsData(List<JobDTO> jobs) {
        List<JobsView.JobDetail> jobsData = new ArrayList<>();
        CompanyDTO companyDTO;
        UserDTO userDTO;
        for(JobDTO job : jobs) {
            companyDTO = companyRepository.findCompanyByCompanyid(job.getCompanyid());
            userDTO = userRepository.findUserByUserid(companyDTO.getUserid());
            jobsData.add(new JobsView.JobDetail(job.getTitle(), job.getSalary(), job.getDescription(), companyDTO.getName(), userDTO.getEmail()));
        }
        return jobsData;
    }

    @Override
    public List<JobDTO> getAllCompanyJobs(int compId) {
        return jobRepository.findJobByCompanyid(compId);
    }

    /**
     * Search for the given keyword in either the job title or the job description
     * @param jobs List of Jobs to filter
     * @param keyword Keyword to filter the List with
     * @return  Filtered List of Jobs
     */
    private List<JobDTO> getFilteredJobs(List<JobDTO> jobs, String keyword) {
        List<JobDTO> matchingJobs = new ArrayList<>();
        for(JobDTO job : jobs) {
            if(job.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            } else if(job.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            }
        }
        return matchingJobs;
    }
}

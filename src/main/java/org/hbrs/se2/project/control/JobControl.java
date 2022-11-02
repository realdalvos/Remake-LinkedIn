package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.factories.JobFactory;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.entities.Job;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

    public void createNewJobPost(JobDTOImpl job) {
        this.jobRepository.save(JobFactory.createJob(job));
    }

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

    public List<JobsView.JobDetail> getAllJobsData(List<JobDTOImpl> jobs) {
        List<JobsView.JobDetail> jobsData = new ArrayList<>();
        CompanyDTO companyDTO;
        UserDTO userDTO;
        for(JobDTOImpl job : jobs) {
            companyDTO = companyRepository.findCompanyByCompanyid(job.getCompanyid());
            userDTO = userRepository.findUserByUserid(companyDTO.getUserid());
            jobsData.add(new JobsView.JobDetail(job.getTitle(), job.getSalary(), job.getDescription(), companyDTO.getName(), userDTO.getEmail()));
        }
        return jobsData;
    }
}


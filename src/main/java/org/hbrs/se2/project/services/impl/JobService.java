package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.control.factories.JobFactory;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.entities.Job;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.services.JobServiceInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService implements JobServiceInterface {

    private final CompanyRepository companyRepository;

    private final JobRepository jobRepository;

    public JobService(CompanyRepository companyRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public CompanyDTO getCompanyByUserid(int id) {
        return companyRepository.findCompanyByUserid(id);
    }

    @Override
    public String getCompanyOfJob(JobDTO job) {
        return companyRepository.findCompanyByCompanyid(job.getCompanyid()).getName();
    }

    @Override
    public void createNewJobPost(JobDTO job) {
        this.jobRepository.save(JobFactory.createJob(job));
    }

    @Override
    public List<JobDTO> getAllCompanyJobs(int compId) {
        return jobRepository.findJobByCompanyid(compId);
    }

    /**
     * Search for the given keyword in either the job title or the job description
     * @param keyword Keyword to filter the List with
     * @return  Filtered List of Jobs
     */
    public List<JobDTO> getJobsMatchingKeyword(String keyword) {
        List<JobDTO> matchingJobs = new ArrayList<>();
        for(JobDTO job : getAllJobs()) {
            if(job.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            } else if(job.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            }
        }
        return matchingJobs;
    }

    /**
     * Removes a job entry in the Database identified by the Job ID
     * @param jobid Job ID
     */
    @Override
    public void removeJob(int jobid) {
        this.jobRepository.deleteById(jobid);
    }

    /**
     * returns all jobs in the DB-table "jobs"
     * @return List<JobDTO> of all Jobs in DB-table
     */
    @Override
    public List<JobDTO> getAllJobs() {
        // empty list jobDTOs
        List<JobDTO> jobDTOs = new ArrayList<>();
        // get all jobs
        List<Job> jobs = jobRepository.findAll();
        // transform all job entities into jobDTOs
        JobDTO jobDTO;
        for(Job job : jobs) {
            jobDTO = new JobDTOImpl(
                    job.getJobid(), job.getCompanyid(), job.getTitle(), job.getDescription(), job.getSalary(), job.getLocation(), job.getContactdetails()
            );
            jobDTOs.add(jobDTO);
        }
        return jobDTOs;
    }
}

package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.services.JobServiceInterface;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService implements JobServiceInterface {

    @Autowired
    private EntityCreationService entityCreationService;

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
        this.jobRepository.save(entityCreationService.jobFactory().createEntity(job));
    }

    @Override
    public List<JobDTO> getAllCompanyJobs(int compId) {
        return jobRepository.findJobByCompanyid(compId);
    }

    /**
     * Search for the given keyword in either the job title or the job description
     *
     * @param keyword Keyword to filter the List with
     * @return Filtered List of Jobs
     */
    public List<JobDTO> getJobsMatchingKeyword(String keyword) {
        List<JobDTO> matchingJobs = new ArrayList<>();
        getAllJobs().parallelStream().forEach(job -> {
            if (job.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            } else if (job.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            }
        });
        return matchingJobs;
    }

    /**
     * Removes a job entry in the Database identified by the Job ID
     *
     * @param jobid Job ID
     */
    @Override
    public void removeJob(int jobid) {
        this.jobRepository.deleteById(jobid);
    }

    /**
     * returns all jobs in the DB-table "jobs"
     *
     * @return List<JobDTO> of all Jobs in DB-table
     */
    @Override
    public List<JobDTO> getAllJobs() {
        return jobRepository.getAll().parallelStream().filter(job -> !companyRepository.findCompanyByCompanyid(job.getCompanyid()).getBanned()).collect(Collectors.toList());
    }
}

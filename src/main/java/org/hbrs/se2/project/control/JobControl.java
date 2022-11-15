package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.services.impl.JobService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class JobControl {
    final JobService jobService;

    public JobControl(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Get the Company of a User
     * @param id id of User
     * @return Company of the User
     */
    public CompanyDTO getCompanyByUserid(int id) {
        return jobService.getCompanyByUserid(id);
    }

    /**
     * Get Company Name for a Job
     * @param job Job
     * @return Company Name for the Job
     */
    public String getCompanyOfJob(JobDTO job) {
        return jobService.getCompanyOfJob(job);
    }

    /**
     * Creates a new Job and saves it in the database
     * @param job Job object with new Job
     */
    public void createNewJobPost(JobDTO job) {
        jobService.createNewJobPost(job);
    }

    /**
     * Sort for Jobs with a specific keyword
     * @param keyword The keyword to search for
     * @return List of jobs that contain the keyword in either the job title or the job description
     */
    public List<JobDTO> getJobsMatchingKeyword(String keyword) {
        return jobService.getJobsMatchingKeyword(keyword);
    }

    /**
     * Get all Jobs of a Company
     * @param compId Company ID
     * @return List of all Jobs
     */
    public List<JobDTO> getAllCompanyJobs(int compId){
        return jobService.getAllCompanyJobs(compId);
    }

    /**
     * Removes a job
     * @param jobid Job ID
     */
    public void deleteJob(int jobid){
        jobService.removeJob(jobid);
    }
}


package org.hbrs.se2.project.services;

import org.hbrs.se2.project.dtos.JobDTO;

import java.util.List;

public interface JobServiceInterface {

    /**
     * Get Company Name for a Job
     * @param job Job
     * @return Company Name for the Job
     */
    String getCompanyOfJob(JobDTO job);

    /**
     * Creates a new Job and saves it in the database
     * @param job Job object with new Job
     */
    void createNewJobPost(JobDTO job);

    /**
     * Sort for Jobs with a specific keyword
     * @param keyword The keyword to search for
     * @return A List of all matching Jobs
     */
    List<JobDTO> getJobsMatchingKeyword(String keyword);

    /**
     * Get all Jobs of a Company
     * @param compId Company ID
     * @return List of all Jobs
     */
    List<JobDTO> getAllCompanyJobs(int compId);

    /**
     * Removes a Job from the database
     * @param jobid Job ID
     */
    void removeJob(int jobid);

    List<JobDTO> getAllJobs();
}

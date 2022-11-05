package org.hbrs.se2.project.services;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.views.studentViews.JobsView;

import java.util.List;

public interface JobServiceInterface {

    /**
     * Get the Company of a User
     * @param id id of User
     * @return Company of the User
     */
    public CompanyDTO getCompanyByUserid(int id);

    /**
     * Creates a new Job and saves it in the database
     * @param job Job object with new Job
     */
    public void createNewJobPost(JobDTO job);

    /**
     * Sort for Jobs with a specific keyword
     * @param keyword The keyword to search for
     * @return A List of all matching Jobs
     */
    public List<JobDTO> getJobsMatchingKeyword(String keyword);

    /**
     * Get all the Data of a List of Jobs
     * @param jobs List of Job Objects
     * @return List of Jobs with all their Details
     */
    public List<JobsView.JobDetail> getAllJobsData(List<JobDTO> jobs);
}

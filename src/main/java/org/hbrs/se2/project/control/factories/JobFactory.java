package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.entities.Job;

public class JobFactory {

    public static Job createJob(JobDTO jobDTO) {
        // create job entity
        Job job = new Job();
        // pass parameters from jobDTO to job
        job.setCompanyid(jobDTO.getCompanyid());
        job.setTitle(jobDTO.getTitle());
        job.setDescription(jobDTO.getDescription());
        job.setSalary(jobDTO.getSalary());
        job.setJobid(jobDTO.getJobid());
        job.setLocation(jobDTO.getLocation());
        job.setContactdetails(jobDTO.getContactdetails());
        return job;
    }
}

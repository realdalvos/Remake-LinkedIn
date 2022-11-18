package org.hbrs.se2.project.control;

import com.vaadin.flow.data.provider.ListDataProvider;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.dtos.TopicDTO;
import org.hbrs.se2.project.services.impl.ProfileService;
import org.springframework.stereotype.Component;

@Component
public class ProfileControl {

    final ProfileService profileService;

    public ProfileControl(ProfileService profileService){
        this.profileService = profileService;
    }

    public void saveStudentData(int id, String major, String university, String topic, String skill) throws DatabaseUserException {
        profileService.saveStudentData(id, major, university, topic, skill);
    }

    public void updateStudyMajor(String major, int userid) throws DatabaseUserException {
        profileService.updateStudyMajor(major, userid);
    }

    // methods are never called, see comments in profile View
    public void updateTopics(String topic, int userid) throws DatabaseUserException {
        profileService.updateTopics(topic, userid);
    }

    // methods are never called, see comments in profile Views
    public void updateSkills(String skill, int userid) throws DatabaseUserException {
        profileService.updateSkills(skill, userid);
    }

    public void updateUniversity(String university, int userid) throws DatabaseUserException {
        profileService.updateUniversity(university, userid);
    }

    public String getUniversityOfStudent(int userid) {
        return profileService.getUniversityOfStudent(userid);
    }

    public ListDataProvider<MajorDTO> getMajorOfStudent(int userid) {
        return profileService.getMajorOfStudent(userid);
    }
    public ListDataProvider<TopicDTO> getTopicOfStudent(int userid){
        return profileService.getTopicOfStudent(userid);
    }

    public ListDataProvider<SkillDTO> getSkillOfStudent(int userid){
        return profileService.getSkillOfStudent(userid);
    }
}




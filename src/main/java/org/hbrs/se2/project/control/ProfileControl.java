package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.dtos.TopicDTO;
import org.hbrs.se2.project.services.impl.ProfileService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileControl {

    final ProfileService profileService;

    public ProfileControl(ProfileService profileService) {
        this.profileService = profileService;
    }

    public void saveStudentData(int id, String university, List<String> major, List<String> topic, List<String> skill) throws DatabaseUserException {
        profileService.saveStudentData(id, university, major, topic, skill);
    }

    public String getUniversityOfStudent(int userid) {
        return profileService.getUniversityOfStudent(userid);
    }

    public List<MajorDTO> getMajorOfStudent(int userid) {
        return profileService.getMajorOfStudent(userid);
    }

    public List<TopicDTO> getTopicOfStudent(int userid) {
        return profileService.getTopicOfStudent(userid);
    }

    public List<SkillDTO> getSkillOfStudent(int userid) {
        return profileService.getSkillOfStudent(userid);
    }

    public void removeMajor(int userid, int majorid) {
        profileService.removeMajor(userid, majorid);
    }

    public void removeTopic(int userid, int topicid) {
        profileService.removeTopic(userid, topicid);
    }

    public void removeSkill(int userid, int skillid) {
        profileService.removeSkill(userid, skillid);
    }

}

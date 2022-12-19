package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.services.impl.ProfileService;
import org.hbrs.se2.project.services.impl.ValidationService;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class ProfileControl {

    final ProfileService profileService;
    final ValidationService validationService;

    public ProfileControl(ProfileService profileService, ValidationService validationService) {
        this.profileService = profileService;
        this.validationService = validationService;
    }

    public void saveStudentData(UserDTO user, StudentDTO student, Set<String> major, Set<String> topic, Set<String> skill) throws DatabaseUserException {
        profileService.saveStudentData(user, student, major, topic, skill);
    }

    public void saveCompanyData(UserDTO user, CompanyDTO company) throws DatabaseUserException {
        profileService.saveCompanyData(user, company);
    }

    public boolean checkUsernameUnique(String username) {
        return validationService.checkUsernameUnique(username);
    }

    public boolean checkEmailUnique(String email) {
        return validationService.checkEmailUnique(email);
    }

    public boolean checkMatrikelnumberUnique(String matrikelnumber) {
        return validationService.checkMatrikelnumberUnique(matrikelnumber);
    }

    public Set<MajorDTO> getMajorOfStudent(int userid) {
        return profileService.getMajorOfStudent(userid);
    }

    public Set<TopicDTO> getTopicOfStudent(int userid) {
        return profileService.getTopicOfStudent(userid);
    }

    public Set<SkillDTO> getSkillOfStudent(int userid) {
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

    public Set<StudentDTO> getStudentsMatchingKeyword(String keyword) {return profileService.getStudentsMatchingKeyword(keyword);}

    public void deleteUser(UserDTO user) throws DatabaseUserException {
        profileService.deleteUser(user);
    }

}

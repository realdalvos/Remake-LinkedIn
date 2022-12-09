package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.services.impl.ProfileService;
import org.hbrs.se2.project.services.impl.ValidationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ProfileControl {

    final ProfileService profileService;
    final ValidationService validationService;

    public ProfileControl(ProfileService profileService, ValidationService validationService) {
        this.profileService = profileService;
        this.validationService = validationService;
    }

    public void saveStudentData(UserDTO user, StudentDTO student, List<String> major, List<String> topic, List<String> skill) throws DatabaseUserException {
        profileService.saveStudentData(user, student, major, topic, skill);
    }

    public void saveCompanyData(UserDTO user, CompanyDTO company) {
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

    public StudentDTO getStudentProfile(int userid) {
        return profileService.getStudentProfile(userid);
    }

    public CompanyDTO getCompanyProfile(int userid) {
        return profileService.getCompanyProfile(userid);
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

    public Set<StudentDTO> getStudentsMatchingKeyword(String keyword) {return profileService.getStudentsMatchingKeyword(keyword);}

    public UserDTO getUserByUserid(int id) {return profileService.getUserByUserid(id);}

    public void deleteUser(UserDTO user) throws DatabaseUserException {
        profileService.deleteUser(user);
    }

    public void saveUserPasswd(UserDTO user) throws DatabaseUserException {
        profileService.saveUserPasswd(user);
    }
}

package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.services.impl.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.Set;

@Controller
public class ProfileControl {

    @Autowired
    ProfileService profileService;

    /**
     * Writes changes to a student's profile to the Database
     * @param user UserDTO
     * @param student StudentDTO
     * @param major Set of Strings to create major entries
     * @param topic Set of Strings to create topic entries
     * @param skill Set of Strings to create skill entries
     * @throws DatabaseUserException Data already exists
     */
    public void saveStudentData(UserDTO user, StudentDTO student, Set<String> major, Set<String> topic, Set<String> skill) throws DatabaseUserException {
        profileService.saveStudentData(user, student, major, topic, skill);
    }

    /**
     * Writes changes to a company's profile to the Database
     * @param user UserDTO
     * @param company CompanyDTO
     * @throws DatabaseUserException Data already exists
     */
    public void saveCompanyData(UserDTO user, CompanyDTO company) throws DatabaseUserException {
        profileService.saveCompanyData(user, company);
    }

    /**
     * Get Set of all majors of a student
     * @param userid User ID of the student
     * @return Set of MajorDTOs
     */
    public Set<MajorDTO> getMajorOfStudent(int userid) {
        return profileService.getMajorOfStudent(userid);
    }

    /**
     * Get Set of all topics of a student
     * @param userid User ID of the student
     * @return Set of TopicDTOs
     */
    public Set<TopicDTO> getTopicOfStudent(int userid) {
        return profileService.getTopicOfStudent(userid);
    }

    /**
     * Get Set of all skills of a student
     * @param userid User ID of the student
     * @return Set of SkillDTOs
     */
    public Set<SkillDTO> getSkillOfStudent(int userid) {
        return profileService.getSkillOfStudent(userid);
    }

    /**
     * Remove a major from a student's profile
     * @param userid User ID of the student
     * @param majorid Major ID
     */
    public void removeMajor(int userid, int majorid) {
        profileService.removeMajor(userid, majorid);
    }

    /**
     * Remove a topic from a student's profile
     * @param userid User ID of the student
     * @param topicid Topic ID
     */
    public void removeTopic(int userid, int topicid) {
        profileService.removeTopic(userid, topicid);
    }

    /**
     * Remove a skill from a student's profile
     * @param userid User ID of the student
     * @param skillid Skill ID
     */
    public void removeSkill(int userid, int skillid) {
        profileService.removeSkill(userid, skillid);
    }

    /**
     * Get a Set of students matching a keyword
     * @return Set of StudentDTOs, where either skills, topics, majors or university match the given keyword
     */
    public Set<StudentDTO> getStudentsMatchingKeyword(String keyword) {return profileService.getStudentsMatchingKeyword(keyword);}

    /**
     * Delete a user
     * @param user UserDTO of the user
     */
    public void deleteUser(UserDTO user) throws DatabaseUserException { profileService.deleteUser(user); }

    /**
     * Change a user's password
     * @param user Updated UserDTO with new password
     */
    public void changeUserPassword(UserDTO user) throws DatabaseUserException { profileService.changeUserPassword(user); }
}

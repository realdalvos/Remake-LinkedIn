package org.hbrs.se2.project.services;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;

import java.util.Set;

public interface ProfileServiceInterface {
    void saveStudentData(UserDTO user, StudentDTO student, Set<String> major, Set<String> topic, Set<String> skill) throws DatabaseUserException;

    void saveCompanyData(UserDTO user, CompanyDTO company);

    Set<MajorDTO> getMajorOfStudent(int userid);

    Set<TopicDTO> getTopicOfStudent(int userid);

    Set<SkillDTO> getSkillOfStudent(int userid);

    StudentDTO getStudentProfile(int userid);

    CompanyDTO getCompanyProfile(int userid);

    void removeMajor(int userid, MajorDTO major);

    void removeTopic(int userid, TopicDTO topic);

    void removeSkill(int userid, SkillDTO skill);
}

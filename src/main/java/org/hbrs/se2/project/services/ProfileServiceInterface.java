package org.hbrs.se2.project.services;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.dtos.TopicDTO;

import java.util.List;

public interface ProfileServiceInterface {
    void saveStudentData(int id, String university, List<String> major, List<String> topic, List<String> skill) throws DatabaseUserException;

    String getUniversityOfStudent(int userid);

    List<MajorDTO> getMajorOfStudent(int userid);

    List<TopicDTO> getTopicOfStudent(int userid);

    List<SkillDTO> getSkillOfStudent(int userid);

    void removeMajor(int userid, int majorid);

    void removeTopic(int userid, int topicid);

    void removeSkill(int userid, int skillid);
}

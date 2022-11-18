package org.hbrs.se2.project.services;

import com.vaadin.flow.data.provider.ListDataProvider;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.dtos.TopicDTO;

public interface ProfileServiceInterface {
     void saveStudentData(int id, String major, String university, String topic, String skill) throws DatabaseUserException;
     void updateStudyMajor(String major, int userid) throws DatabaseUserException;
     void updateTopics(String topic, int userid) throws DatabaseUserException;
     void updateSkills(String skill, int userid) throws DatabaseUserException;
     void updateUniversity(String university, int userid) throws DatabaseUserException;
     String getUniversityOfStudent(int userid);
     ListDataProvider<MajorDTO> getMajorOfStudent(int userid);
     ListDataProvider<TopicDTO> getTopicOfStudent(int userid);
     ListDataProvider<SkillDTO> getSkillOfStudent(int userid);
}

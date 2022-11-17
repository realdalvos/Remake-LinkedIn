package org.hbrs.se2.project.services;

import org.hbrs.se2.project.control.exception.DatabaseUserException;

import java.util.List;

public interface ProfileServiceInterface {
     void saveStudentData(int id, String major, String university, String topic, String skill) throws DatabaseUserException;
     void updateStudyMajor(String major, int userid) throws DatabaseUserException;
     void updateTopics(String topic, int userid) throws DatabaseUserException;
     void updateSkills(String skill, int userid) throws DatabaseUserException;
     void updateUniversity(String university, int userid) throws DatabaseUserException;
     String getUniversityOfStudent(int userid);
     List<String> getMajorOfStudent(int userid);
     List<String> getTopicOfStudent(int userid);
     List<String> getSkillOfStudent(int userid);
}

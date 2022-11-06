package org.hbrs.se2.project.dtos;

public interface StudentDTO {

    int getStudentid();

    int getUserid();

    String getFirstname();

    String getLastname();

    String getMatrikelnumber();

    String getStudyMajor();

    String getUniversity();

    void setStudentId(int id);

    void setUserid(int userid);

    void setFirstname(String firstname);

    void setLastname(String lastname);

    void setMatrikelnumber(String matrikelnumber);

    void setStudyMajor(String studyMajor);

    void setUniversity(String university);
}

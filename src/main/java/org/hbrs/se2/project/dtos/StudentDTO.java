package org.hbrs.se2.project.dtos;

public interface StudentDTO {

    public int getStudentid();
    public int getUserid();

    public String getFirstname();

    public String getLastname();

    public String getMatrikelnumber();

    public String getStudyMajor();

    public String getUniversity();

    void setStudentId(int id);

    void setUserid(int userid);

    void setFirstname(String firstname);

    void setLastname(String lastname);

    void setMatrikelnumber(String matrikelnumber);

    void setStudyMajor(String studyMajor);

    public void setUniversity(String university);
}

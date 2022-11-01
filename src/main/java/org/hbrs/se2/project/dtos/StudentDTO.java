package org.hbrs.se2.project.dtos;

public interface StudentDTO {
    public int getStudentid();

    public int getUserid();

    public void setUserid(int userid);

    public String getFirstname();

    public void setFirstname(String firstname);

    public String getLastname();

    public void setLastname(String lastname);

    public int getMatrikelnumber();

    public void setMatrikelnumber(int matrikelnumber);

    public String getStudyMajor();

    public void setStudyMajor(String studyMajor);

    public String getUniversity();

    public void setUniversity(String university);

    public void setStudentId(int id);
}

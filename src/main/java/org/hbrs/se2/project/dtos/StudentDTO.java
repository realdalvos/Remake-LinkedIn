package org.hbrs.se2.project.dtos;

import java.io.Serializable;

public interface StudentDTO extends Serializable {
    int getStudentid();

    int getUserid();

    String getFirstname();

    String getLastname();

    String getMatrikelnumber();

    String getUniversity();

    void setStudentid(int id);

    void setUserid(int userid);

    void setFirstname(String firstname);

    void setLastname(String lastname);

    void setMatrikelnumber(String matrikelnumber);

    void setUniversity(String university);

}

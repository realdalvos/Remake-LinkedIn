package org.hbrs.se2.project.dtos;

import java.io.Serializable;

public interface UserDTO extends Serializable {
    int getUserid();

    void setUserid(int id);

    String getUsername();

    void setUsername(String name);

    String getPassword();

    void setPassword(String password);

    String getEmail();

    void setEmail(String email);

    String getRole();

    void setRole(String role);
}

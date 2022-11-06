package org.hbrs.se2.project.dtos;

public interface UserDTO {
    public int getUserid();

    public void setUserid(int id);

    public String getUsername();

    public void setUsername(String name);

    public String getPassword();

    public void setPassword(String password);

    public String getEmail();

    public void setEmail(String email);

    public String getRole();

    public void setRole(String role);
}

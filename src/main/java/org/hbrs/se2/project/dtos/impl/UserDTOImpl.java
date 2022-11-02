package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.UserDTO;

public class UserDTOImpl implements UserDTO {
    private int userid;
    private String username;
    private String password;
    private String email;
    private String role;
    public UserDTOImpl(){}
    public UserDTOImpl(String username, String password, String email, String role){
        this.username=username;
        this.password=password;
        this.email=email;
        this.role=role;
    }
    public void setUserid(int id) {this.userid = id;}
    public void setUsername(String name) {this.username = name;}
    public void setPassword(String password) {this.password = password;}
    public void setEmail(String email) {this.email = email;}
    public void setRole(String role) {this.role = role;}
    @Override
    public int getUserid() {return this.userid;}
    @Override
    public String getUsername() {return this.username;}
    @Override
    public String getPassword() {return this.password;}
    @Override
    public String getEmail() {return this.email;}
    @Override
    public String getRole() {return this.role;}
    @Override
    public String toString() {
        return "UserDTOImpl{" +
                "userid= " + userid +
                ", username= '" + username + '\'' +
                ", password= '" + password + '\'' +
                ", email= " + email +
                '}';
    }
}

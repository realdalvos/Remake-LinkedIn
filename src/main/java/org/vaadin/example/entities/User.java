package org.vaadin.example.entities;

import javax.persistence.*;

@Entity
@Table(name="user", schema = "mid9db")
public class User {
    private int userid;
    private String username;
    private String password;
    private String email;
    @Id
    @GeneratedValue
    @Column(name="userid")
    public int getUserid() {
        return userid;
    }
    public void setUserid(int id) {
        this.userid = id;
    }
    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Basic
    @Column(name="password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Basic
    @Column(name="emailaddr")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}


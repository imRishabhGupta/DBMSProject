package com.project.dbms.user.dbmsproject;

/**
 * Created by user on 24-04-2016.
 */
public class User {
    String email;
    String username;
    String password;
    public User(String email,String username,String password){
        this.email=email;
        this.username=username;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}

package com.spl2.givematch.model;

import com.spl2.givematch.model.Role;

public class User {

    private int id;
    private String name;
    private String username;
    private String hashedPassword;
    private Role role;
    private String contactInfo;
    private int priorityLevel;

    public User() {
    }

    public User(int id, String name, String username, String hashedPassword,
                Role role, String contactInfo, int priorityLevel) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.contactInfo = contactInfo;
        this.priorityLevel = priorityLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
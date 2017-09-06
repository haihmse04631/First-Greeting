package com.example.haihm.firstgreeting;

/**
 * Created by haihm on 8/10/2017.
 */

public class User {
    String name;
    String linkAvatar;
    String id;
    String role;

    public User() {
    }

    public User(String name, String linkAvatar, String id, String role) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.id = id;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return name;
    }
}

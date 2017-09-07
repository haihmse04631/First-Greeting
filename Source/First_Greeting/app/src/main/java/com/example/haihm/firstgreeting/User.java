package com.example.haihm.firstgreeting;

import java.util.Date;

/**
 * Created by haihm on 8/10/2017.
 */

public class User {
    String name;
    String linkAvatar;
    String id;
    String role;
    Date lastMessage;

    public User() {
    }

    public User(String name, String linkAvatar, String id, String role) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.id = id;
        this.role = role;
        lastMessage = new Date();
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

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return name;
    }
}

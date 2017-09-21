package com.example.haihm.firstgreeting.message;

import java.util.HashMap;

/**
 * Created by haihm on 8/10/2017.
 */

public class User {
    String name;
    String linkAvatar;
    String id;
    String role;
    HashMap<String, SingleMessage> lastMessage;

    public User() {
    }

    public User(String name, String linkAvatar, String id, String role) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.id = id;
        this.role = role;
        lastMessage = new HashMap<String, SingleMessage>() {
            @Override
            public String toString() {
                String s = "";
                for (String key : lastMessage.keySet()) {
                    s += lastMessage.get(key) + "\n";
                }
                return s;
            }
        };
    }

    public User(String name, String linkAvatar, String id, String role, HashMap<String, SingleMessage> lastMessage) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.id = id;
        this.role = role;
        this.lastMessage = lastMessage;
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

    public HashMap<String, SingleMessage> getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(HashMap<String, SingleMessage> lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return name + "\n" + lastMessage;
    }
}

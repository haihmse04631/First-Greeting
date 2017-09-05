package com.example.haihm.firstgreeting;

import java.util.List;

/**
 * Created by haihm on 8/10/2017.
 */

public class User {
     String name;
     String linkAvatar;
     String id;
     List<MessageToId> messageToIdList;
    String type;



    public List<MessageToId> getMessageToIdList() {
        return messageToIdList;
    }

    public void setMessageToIdList(List<MessageToId> messageToIdList) {
        this.messageToIdList = messageToIdList;
    }

    public User(String name, String linkAvatar, String id, List<MessageToId> messageToIdList) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.id = id;
        this.messageToIdList = messageToIdList;
        this.type = "Member";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String name, String linkAvatar, String id) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.id = id;
    }


    public User(String name, String linkAvatar) {
        this.name = name;
        this.linkAvatar = linkAvatar;
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
}

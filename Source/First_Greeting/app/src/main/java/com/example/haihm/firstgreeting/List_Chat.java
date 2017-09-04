package com.example.haihm.firstgreeting;

/**
 * Created by haihm on 8/10/2017.
 */

public class List_Chat {
     String name;
     String linkAvatar;
     String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List_Chat(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public List_Chat(String name, String linkAvatar, String id) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.id = id;
    }

    public List_Chat() {
    }

    public List_Chat(String name, String linkAvatar) {
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

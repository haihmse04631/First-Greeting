package com.example.haihm.firstgreeting;

/**
 * Created by DuyNguyen on 9/16/2017.
 */

public class UserStatus {
    String name;
    String contentPost;
    String linkAvatar;

    public UserStatus() {

    }

    public UserStatus(String name, String contentPost, String linkAvatar) {
        this.name = name;
        this.contentPost = contentPost;
        this.linkAvatar = linkAvatar;
    }

    public String getContentPost() {
        return contentPost;
    }

    public void setContentPost(String contentPost) {
        this.contentPost = contentPost;
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

    @Override
    public String toString() {
        return "UserStatus{" +
                "name='" + name + '\'' +
                '}';
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }
}

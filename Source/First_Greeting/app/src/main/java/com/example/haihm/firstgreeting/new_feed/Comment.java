package com.example.haihm.firstgreeting.new_feed;

/**
 * Created by DuyNguyen on 9/17/2017.
 */

public class Comment {
    String  linkAvatar;
    String commentUser;
    String  nameUser;

    public Comment(){

    }

    public Comment(String linkAvatar, String commentUser, String nameUser) {
        this.linkAvatar = linkAvatar;
        this.commentUser = commentUser;
        this.nameUser = nameUser;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
}

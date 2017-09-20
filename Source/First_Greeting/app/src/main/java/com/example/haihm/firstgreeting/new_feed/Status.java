package com.example.haihm.firstgreeting.new_feed;

import java.util.ArrayList;

/**
 * Created by DuyNguyen on 9/16/2017.
 */

public class Status {
    private String name;
    private String contentPost;
    private String linkAvatar;
    private int likedNumber;
    private int commentedNumber;
    ArrayList<String> likedUsers;

    public Status() {

    }

    public Status(String name, String contentPost, String linkAvatar) {
        this.name = name;
        this.contentPost = contentPost;
        this.linkAvatar = linkAvatar;
        this.likedNumber = 0;
        this.commentedNumber = 0;
        likedUsers = new ArrayList<>();
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

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public int getLikedNumber() {
        return likedNumber;
    }

    public void setLikedNumber(int likedNumber) {
        this.likedNumber = likedNumber;
    }

    public int getCommentedNumber() {
        return commentedNumber;
    }

    public void setCommentedNumber(int commentedNumber) {
        this.commentedNumber = commentedNumber;
    }

    @Override
    public String toString() {
        return "Status{" +
                "name='" + name + '\'' +
                '}';
    }


}

package com.example.haihm.firstgreeting.video_call;

/**
 * Created by HongSonPham on 9/17/17.
 */

public class Member {
    private String name;
    private String avatar;

    public Member(String name, String avatar) {

        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}

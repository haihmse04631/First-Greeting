package com.example.haihm.firstgreeting.video_call;

/**
 * Created by HongSonPham on 9/17/17.
 */

public class Member {
    private String member1;
    private String member2;
    private String member3;
//    private String imgAvatar1;
//    private String imgAvatar2;
//private String imgAvatar3;

//    public String getImgAvatar1() {
//        return imgAvatar1;
//    }
//
//    public void setImgAvatar1(String imgAvatar1) {
//        this.imgAvatar1 = imgAvatar1;
//    }
//
//    public String getImgAvatar2() {
//        return imgAvatar2;
//    }
//
//    public void setImgAvatar2(String imgAvatar2) {
//        this.imgAvatar2 = imgAvatar2;
//    }

//    public String getImgAvatar3() {
//        return imgAvatar3;
//    }
//
//    public void setImgAvatar3(String imgAvatar3) {
//        this.imgAvatar3 = imgAvatar3;
//    }


    public String getMember1() {
        return member1;
    }

    public void setMember1(String member1) {
        this.member1 = member1;
    }

    public String getMember2() {
        return member2;
    }

    public void setMember2(String member2) {
        this.member2 = member2;
    }

    public String getMember3() {
        return member3;
    }

    public void setMember3(String member3) {
        this.member3 = member3;
    }

    public Member(String member1, String member2, String member3) {
        this.member1 = member1;
        this.member2 = member2;
        this.member3 = member3;
    }

    @Override
    public String toString() {
        return "Member{" +
                "member1='" + member1 + '\'' +
                ", member2='" + member2 + '\'' +
                ", member3='" + member3 + '\'' +
                '}';
    }
}

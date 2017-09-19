package com.example.haihm.firstgreeting.video_call;

/**
 * Created by HongSonPham on 9/20/17.
 */

public class Room {
    private String name1;
    private String name2;
    private String name3;
    private String img1;
    private String img2;
    private String img3;

    public Room(Member mem1, Member mem2, Member mem3) {
        this.name1 = mem1.getName();
        this.name2 = mem2.getName();
        this.name3 = mem3.getName();
        this.img1 = mem1.getAvatar();
        this.img2 = mem2.getAvatar();
        this.img3 = mem3.getAvatar();
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3='" + name3 + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                '}';
    }
}

package com.example.haihm.firstgreeting;

import java.util.Date;

/**
 * Created by haihm on 9/6/2017.
 */

public class SingleMessage {
    private Date date;
    private String content;
    private String avatarLink;
    private String type;

    public SingleMessage() {
    }

    public SingleMessage(Date date, String content, String avatarLink) {
        this.date = date;
        this.content = content;
        this.avatarLink = avatarLink;
        type = "";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    @Override
    public String toString() {
        return "SingleMessage{" +
                "date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}

package com.example.haihm.firstgreeting;

import java.util.Date;

/**
 * Created by haihm on 9/6/2017.
 */

public class SingleMessage {
    private Date date;
    private String content;

    public SingleMessage() {
    }

    public SingleMessage(Date date, String content) {
        this.date = date;
        this.content = content;
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

    @Override
    public String toString() {
        return "SingleMessage{" +
                "date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}

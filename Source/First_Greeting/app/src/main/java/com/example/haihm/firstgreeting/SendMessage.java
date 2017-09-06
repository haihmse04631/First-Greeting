package com.example.haihm.firstgreeting;

/**
 * Created by haihm on 9/6/2017.
 */

public class SendMessage {
    private String messageSend;
    private String linkAvatar;

    public SendMessage(String messageSend, String linkAvatar) {
        this.messageSend = messageSend;
        this.linkAvatar = linkAvatar;
    }

    public String getMessageSend() {
        return messageSend;
    }

    public void setMessageSend(String messageSend) {
        this.messageSend = messageSend;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }
}

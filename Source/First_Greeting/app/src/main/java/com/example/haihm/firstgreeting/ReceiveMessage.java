package com.example.haihm.firstgreeting;

/**
 * Created by haihm on 9/6/2017.
 */

public class ReceiveMessage {
    private String messageReceive;
    private String linkAvatar;

    public ReceiveMessage(String messageReceive, String linkAvatar) {
        this.messageReceive = messageReceive;
        this.linkAvatar = linkAvatar;
    }

    public String getMessageReceive() {
        return messageReceive;
    }

    public void setMessageReceive(String messageReceive) {
        this.messageReceive = messageReceive;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }
}

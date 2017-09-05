package com.example.haihm.firstgreeting;

import java.util.List;

/**
 * Created by haihm on 9/6/2017.
 */

public class MessageToId {
    private String id;
    private List<Message> messageList;

    public MessageToId(String id, List<Message> messageList) {
        this.id = id;
        this.messageList = messageList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}

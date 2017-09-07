package com.example.haihm.firstgreeting;

import java.util.ArrayList;

/**
 * Created by HongSonPham on 9/8/17.
 */

public class MessageList extends ArrayList<SingleMessage> {
    public MessageList() {
        super();
    }

    public synchronized void addMess(SingleMessage aMess) {
        add(aMess);
    }
}

package com.smsbooker.pack.models;

import java.util.Map;

/**
 * Created by Yuriy on 05.05.2014.
 */
public class Message {
    public long id = 0;
    public long threadId = 0;
    public String fromAddress = null;
    public String messageBody = null;
    public long timestamp = 0;
    public boolean isRead = false;

    public Message(long id, long threadId, String fromAddress, String messageBody, long timestamp, boolean isRead){
        this.id = id;
        this.threadId = threadId;
        this.fromAddress = fromAddress;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    @Override
    public String toString(){
        return "ID("+ this.id + ") " + this.fromAddress + "\n[" + this.messageBody + "]";
    }
}

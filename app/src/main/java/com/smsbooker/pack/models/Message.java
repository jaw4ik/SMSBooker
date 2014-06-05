package com.smsbooker.pack.models;

import java.util.Map;

/**
 * Created by Yuriy on 05.05.2014.
 */
public class Message {
    public long id;
    public long threadId;
    public String fromAddress;
    public String messageBody;
    public long timestamp;
    public boolean isRead;

    public Message(String messageBody, long timestamp) {
        this.messageBody = messageBody;
        this.timestamp = timestamp;
    }

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
        return this.messageBody;
    }
}

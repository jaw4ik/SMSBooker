package com.smsbooker.pack.models;

/**
 * Created by Yuriy on 12.05.2014.
 */
public class MessagePart {

    public boolean isNumber;
    public String value;

    public MessagePart(boolean isNumber, String value) {
        this.isNumber = isNumber;
        this.value = value;
    }
}

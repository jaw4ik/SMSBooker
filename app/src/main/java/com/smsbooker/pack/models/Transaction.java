package com.smsbooker.pack.models;

/**
 * Created by Yuriy on 26.05.2014.
 */
public class Transaction implements Comparable<Transaction> {
    public int id;
    public int cardId;
    public Type type;
    public float value;
    public float balance;
    public long createdOn;
    public String message;

    public enum Type{
        increment,
        decrement
    }

    public Transaction() {
    }

    public Transaction(int id, int cardId, String type, float value, float balance, long createdOn, String message) {
        this.id = id;
        this.cardId = cardId;
        this.type = Transaction.Type.valueOf(type);
        this.value = value;
        this.balance = balance;
        this.createdOn = createdOn;
        this.message = message;
    }

    //Comparable implementation
    @Override
    public int compareTo(Transaction another) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this.createdOn < another.createdOn){
            return BEFORE;
        } else if (this.createdOn > another.createdOn){
            return AFTER;
        } else {
            return EQUAL;
        }
    }
}

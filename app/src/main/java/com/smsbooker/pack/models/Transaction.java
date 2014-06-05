package com.smsbooker.pack.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yuriy on 26.05.2014.
 */
public class Transaction implements Comparable<Transaction>, Parcelable {
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

    //Parcelable implementation
    public Transaction(Parcel source) {
        this.id = source.readInt();
        this.cardId = source.readInt();
        this.type = (Transaction.Type)source.readSerializable();
        this.value = source.readFloat();
        this.balance = source.readFloat();
        this.createdOn = source.readLong();
        this.message = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.cardId);
        dest.writeSerializable(this.type);
        dest.writeFloat(this.value);
        dest.writeFloat(this.balance);
        dest.writeLong(this.createdOn);
        dest.writeString(this.message);
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}

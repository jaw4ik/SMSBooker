package com.smsbooker.pack.models;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.smsbooker.pack.TransactionsManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class Card implements Parcelable {
    public int id;
    public String code;
    public String name;

    public ArrayList<CardPattern> cardPatterns;
    public ArrayList<Transaction> transactions;

    public float getBalance(){
        int transactionsCount = transactions.size();
        if (transactionsCount > 0){
            return transactions.get(transactionsCount - 1).balance;
        }
        return 0;
    }

    public Card(String name, ArrayList<CardPattern> cardPatterns) {
        this.name = name;
        this.cardPatterns = cardPatterns;
    }

    public Card (int id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public void createTransactions(Context context){
        this.transactions = TransactionsManager.getTransactionsForCard(context, this);
    }

    //Parcelable implementation
    public Card(Parcel source) {
        this.id = source.readInt();
        this.code = source.readString();
        this.name = source.readString();

        this.cardPatterns = new ArrayList<CardPattern>();
        source.readList(this.cardPatterns, CardPattern.class.getClassLoader());

        this.transactions = new ArrayList<Transaction>();
        source.readList(this.transactions, Transaction.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeString(this.name);

        dest.writeList(this.cardPatterns);
        dest.writeList(this.transactions);
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public String toString(){
        return this.name;
    }
}

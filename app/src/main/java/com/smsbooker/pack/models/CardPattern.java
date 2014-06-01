package com.smsbooker.pack.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yuriy on 26.05.2014.
 */
public class CardPattern implements Parcelable {
    public int id;
    public int cardId;
    public String address;
    public Transaction.Type transactionType;
    public String checkWord;

    public ValuePattern quantityValuePattern;
    public ValuePattern balanceValuePattern;

    public CardPattern() {
    }

    public CardPattern(Bundle bundle) {
        if (bundle == null){
            return;
        }

        this.address = bundle.getString("address");
        this.transactionType = (Transaction.Type)bundle.getSerializable("transactionType");
        this.checkWord = bundle.getString("checkword");

        this.quantityValuePattern = new ValuePattern(bundle, "quantity");
        this.balanceValuePattern = new ValuePattern(bundle, "balance");
    }

    public CardPattern(int id, int cardId, String address, String transactionType, String checkWord, ValuePattern quantityValuePattern, ValuePattern balanceValuePattern) {
        this.id = id;
        this.cardId = cardId;
        this.address = address;
        this.transactionType = Transaction.Type.valueOf(transactionType);
        this.checkWord = checkWord;
        this.quantityValuePattern = quantityValuePattern;
        this.balanceValuePattern = balanceValuePattern;
    }

    //Parcelable implementation
    public CardPattern(Parcel source) {
        this.id = source.readInt();
        this.cardId = source.readInt();
        this.address = source.readString();
        this.transactionType = (Transaction.Type)source.readSerializable();
        this.checkWord = source.readString();

        this.quantityValuePattern = source.readParcelable(ValuePattern.class.getClassLoader());
        this.balanceValuePattern = source.readParcelable(ValuePattern.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.cardId);
        dest.writeString(this.address);
        dest.writeSerializable(this.transactionType);
        dest.writeString(this.checkWord);

        dest.writeParcelable(this.quantityValuePattern, flags);
        dest.writeParcelable(this.balanceValuePattern, flags);
    }

    public static final Creator<CardPattern> CREATOR = new Creator<CardPattern>() {
        @Override
        public CardPattern createFromParcel(Parcel source) {
            return new CardPattern(source);
        }

        @Override
        public CardPattern[] newArray(int size) {
            return new CardPattern[size];
        }
    };
}

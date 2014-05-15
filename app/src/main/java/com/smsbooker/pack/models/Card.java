package com.smsbooker.pack.models;

import android.os.Bundle;

import java.util.Map;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class Card {
    public int id;
    public String code;
    public String name;
    public String phoneAddress;
    public float balance;
    public Pattern pattern;

    public Card() {
        pattern = new Pattern();
    }

    public Card (int id, String code, String name, String phoneAddress, float balance, String previousPattern, String nextPattern){
        this();

        this.id = id;
        this.code = code;
        this.name = name;
        this.phoneAddress = phoneAddress;
        this.balance = balance;

        this.pattern.previousText = previousPattern;
        this.pattern.nextText = nextPattern;
    }

    public Card (String name, String phoneAddress, float balance){
        this(0, null, name, phoneAddress, balance, null, null);
    }

    public Card (Bundle bundle){
        this();

        this.id = bundle.getInt("id");
        this.code = bundle.getString("code");
        this.name = bundle.getString("name");
        this.phoneAddress = bundle.getString("phoneAddress");
        this.balance = bundle.getFloat("balance");

        this.pattern.previousText = bundle.getString("previousPattern");
        this.pattern.nextText = bundle.getString("nextPattern");
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();

        bundle.putInt("id", this.id);
        bundle.putString("code", this.code);
        bundle.putString("name", this.name);
        bundle.putString("phoneAddress", this.phoneAddress);
        bundle.putFloat("balance", this.balance);

        if (this.pattern != null) {
            bundle.putString("previousPattern", this.pattern.previousText);
            bundle.putString("nextPattern", this.pattern.nextText);
        }

        return bundle;
    }

    public class Pattern{
        public String previousText;
        public String nextText;
    }
}

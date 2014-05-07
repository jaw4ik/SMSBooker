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

    public Card (int id, String code, String name, String phoneAddress, float balance){
        this.id = id;
        this.code = code;
        this.name = name;
        this.phoneAddress = phoneAddress;
        this.balance = balance;
    }

    public Card (String name, String phoneAddress, float balance){
        this.name = name;
        this.phoneAddress = phoneAddress;
        this.balance = balance;
    }

    public Card (Bundle bundle){
        this.code = bundle.getString("code");
        this.name = bundle.getString("name");
        this.phoneAddress = bundle.getString("phoneAddress");
        this.balance = bundle.getFloat("balance");
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();

        bundle.putString("code", code);
        bundle.putString("name", name);
        bundle.putString("phoneAddress", phoneAddress);
        bundle.putFloat("balance", balance);

        return bundle;
    }
}

package com.smsbooker.pack.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.DBManager;
import com.smsbooker.pack.models.Card;

import java.util.ArrayList;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class CardsRepository {

    final String TABLE_NAME = "cards";

    DBManager dbManager;

    public CardsRepository(Context context){
        dbManager = new DBManager(context);
    }

    public ArrayList<Card> getAll(){
        SQLiteDatabase db = dbManager.getDB();

        ArrayList<Card> cards = new ArrayList<Card>();

        try{
            Cursor cursor = db.query("cards", null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()){
                do{
                    cards.add(new Card(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("code")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("phoneAddress")),
                            cursor.getFloat(cursor.getColumnIndex("balance"))
                    ));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally{
            dbManager.close();
        }

        return cards;
    }

    public void add(Card card){
        SQLiteDatabase db = dbManager.getDB();
        ContentValues values = new ContentValues();

        values.put("code", card.code);
        values.put("name", card.name);
        values.put("phoneAddress", card.phoneAddress);
        values.put("balance", card.balance);

        db.insert(TABLE_NAME, null, values);

        dbManager.close();
    }
}

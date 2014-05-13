package com.smsbooker.pack.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.DBManager;
import com.smsbooker.pack.models.Card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class CardsRepository {

    final String TABLE_NAME = "cards";

    private static class ColumnsNames{
        public static String id = "id";
        public static String code = "code";
        public static String name = "name";
        public static String phoneAddress = "phoneAddress";
        public static String balance = "balance";
    }

    DBManager dbManager;

    public CardsRepository(Context context){
        dbManager = new DBManager(context);
    }

    public ArrayList<Card> getAll(){
        SQLiteDatabase db = dbManager.getDB();

        ArrayList<Card> cards = new ArrayList<Card>();

        try{
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()){
                do{
                    cards.add(new Card(
                            cursor.getInt(cursor.getColumnIndex(ColumnsNames.id)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.code)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.name)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.phoneAddress)),
                            cursor.getFloat(cursor.getColumnIndex(ColumnsNames.balance))
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

        values.put(ColumnsNames.code, card.code);
        values.put(ColumnsNames.name, card.name);
        values.put(ColumnsNames.phoneAddress, card.phoneAddress);
        values.put(ColumnsNames.balance, card.balance);

        db.insert(TABLE_NAME, null, values);

        dbManager.close();
    }

    public void delete(int id){
        SQLiteDatabase db = dbManager.getDB();

        db.delete(TABLE_NAME, ColumnsNames.id + " = ?", new String[] {Integer.toString(id)});

        dbManager.close();
    }

    public void update(Card card){
        SQLiteDatabase db = dbManager.getDB();

        ContentValues values = new ContentValues();

        values.put(ColumnsNames.code, card.code);
        values.put(ColumnsNames.name, card.name);
        values.put(ColumnsNames.phoneAddress, card.phoneAddress);
        values.put(ColumnsNames.balance, card.balance);

        db.update(TABLE_NAME,values,ColumnsNames.id + " = ?", new String[] {Integer.toString(card.id)});

        dbManager.close();
    }
}

package com.smsbooker.pack.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.DBManager;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.ValuePattern;

import java.util.ArrayList;

/**
 * Created by Yuriy on 28.05.2014.
 */
public class CardPatternsRepository {
    public static final String TABLE_NAME = "card_patterns";

    public static class ColumnsNames{
        public static String id = "id";
        public static String cardId = "cardId";
        public static String address = "address";
        public static String transactionType = "transactionType";
        public static String checkWord = "checkWord";
        public static String quantityPreviousValue = "quantityPreviousValue";
        public static String quantityNextValue = "quantityNextValue";
        public static String balancePreviousValue = "balancePreviousValue";
        public static String balanceNextValue = "balanceNextValue";
    }

    DBManager dbManager;

    public CardPatternsRepository(Context context) {
        this.dbManager = new DBManager(context);
    }

    public ArrayList<CardPattern> getAll(){
        SQLiteDatabase db = dbManager.getDB();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        return getAllFromCursor(cursor);
    }

    public ArrayList<CardPattern> getCardPatternsByCardId(int cardId){
        SQLiteDatabase db = dbManager.getDB();
        Cursor cursor = db.query(TABLE_NAME, null, ColumnsNames.cardId + " = ?", new String[]{ Integer.toString(cardId) }, null, null, null);

        return getAllFromCursor(cursor);
    }

    public ArrayList<CardPattern> getCardPatternsByAddress(String address){
        SQLiteDatabase db = dbManager.getDB();
        Cursor cursor = db.query(TABLE_NAME, null, ColumnsNames.address + " = ?", new String[]{ address }, null, null, null);

        return getAllFromCursor(cursor);
    }

    private ArrayList<CardPattern> getAllFromCursor(Cursor cursor){
        ArrayList<CardPattern> cardPatterns = new ArrayList<CardPattern>();

        try{
            if (cursor != null && cursor.moveToFirst()){
                do{
                    ValuePattern quantityValuePattern = new ValuePattern(
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.quantityPreviousValue)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.quantityNextValue))
                    );

                    ValuePattern balanceValuePattern = new ValuePattern(
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.balancePreviousValue)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.balanceNextValue))
                    );

                    CardPattern cardPattern = new CardPattern(
                            cursor.getInt(cursor.getColumnIndex(ColumnsNames.id)),
                            cursor.getInt(cursor.getColumnIndex(ColumnsNames.cardId)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.address)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.transactionType)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.checkWord)),
                            quantityValuePattern,
                            balanceValuePattern
                    );

                    cardPatterns.add(cardPattern);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally{
            dbManager.close();
        }

        return cardPatterns;
    }

    public void add(CardPattern cardPattern){
        SQLiteDatabase db = dbManager.getDB();
        ContentValues values = new ContentValues();

        values.put(ColumnsNames.cardId, cardPattern.cardId);
        values.put(ColumnsNames.address, cardPattern.address);
        values.put(ColumnsNames.transactionType, cardPattern.transactionType.toString());
        values.put(ColumnsNames.checkWord, cardPattern.checkWord);
        values.put(ColumnsNames.quantityPreviousValue, cardPattern.quantityValuePattern.previousText);
        values.put(ColumnsNames.quantityNextValue, cardPattern.quantityValuePattern.nextText);
        values.put(ColumnsNames.balancePreviousValue, cardPattern.balanceValuePattern.previousText);
        values.put(ColumnsNames.balanceNextValue, cardPattern.balanceValuePattern.nextText);

        cardPattern.id = (int)db.insert(TABLE_NAME, null, values);

        dbManager.close();
    }

    public void delete(int id){
        SQLiteDatabase db = dbManager.getDB();

        db.delete(TABLE_NAME, ColumnsNames.id + " = ?", new String[] {Integer.toString(id)});

        dbManager.close();
    }

    public void deleteAllByCardId(int cardId){
        SQLiteDatabase db = dbManager.getDB();

        db.delete(TABLE_NAME, ColumnsNames.cardId + " = ?", new String[] {Integer.toString(cardId)});

        dbManager.close();
    }
}

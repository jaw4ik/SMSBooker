package com.smsbooker.pack.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.DBManager;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Transaction;
import com.smsbooker.pack.models.ValuePattern;

import java.util.ArrayList;

/**
 * Created by Yuriy on 31.05.2014.
 */
public class TransactionsRepository {

    public static final String TABLE_NAME = "transactions";

    public static class ColumnsNames{
        public static String id = "id";
        public static String cardId = "cardId";
        public static String type = "type";
        public static String value = "value";
        public static String balance = "balance";
        public static String createdOn = "createdOn";
        public static String message = "message";
    }

    DBManager dbManager;

    public TransactionsRepository(Context context) {
        dbManager = new DBManager(context);
    }

    public ArrayList<Transaction> getTransactionsByCardId(int cardId){
        SQLiteDatabase db = dbManager.getDB();

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        try{
            Cursor cursor = db.query(TABLE_NAME, null, ColumnsNames.cardId + " = ?", new String[]{ Integer.toString(cardId) }, null, null, ColumnsNames.createdOn);
            if (cursor != null && cursor.moveToFirst()){
                do{
                    Transaction transaction = new Transaction(
                            cursor.getInt(cursor.getColumnIndex(ColumnsNames.id)),
                            cursor.getInt(cursor.getColumnIndex(ColumnsNames.cardId)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.type)),
                            cursor.getFloat(cursor.getColumnIndex(ColumnsNames.value)),
                            cursor.getFloat(cursor.getColumnIndex(ColumnsNames.balance)),
                            cursor.getLong(cursor.getColumnIndex(ColumnsNames.createdOn)),
                            cursor.getString(cursor.getColumnIndex(ColumnsNames.message))
                    );

                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally{
            dbManager.close();
        }

        return transactions;
    }

    public void add(Transaction transaction){
        SQLiteDatabase db = dbManager.getDB();
        ContentValues values = new ContentValues();

        values.put(ColumnsNames.cardId, transaction.cardId);
        values.put(ColumnsNames.type, transaction.type.toString());
        values.put(ColumnsNames.value, transaction.value);
        values.put(ColumnsNames.balance, transaction.balance);
        values.put(ColumnsNames.createdOn, transaction.createdOn);
        values.put(ColumnsNames.message, transaction.message);

        transaction.id = (int)db.insert(TABLE_NAME, null, values);

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

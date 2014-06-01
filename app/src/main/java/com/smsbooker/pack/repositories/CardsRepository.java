package com.smsbooker.pack.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.DBManager;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Transaction;
import com.smsbooker.pack.models.ValuePattern;

import java.util.ArrayList;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class CardsRepository {

    public static final String TABLE_NAME = "cards";

    public static class ColumnsNames{
        public static String id = "id";
        public static String code = "code";
        public static String name = "name";
        public static String balance = "balance";
    }

    DBManager dbManager;
    Context context;

    public CardsRepository(Context context){
        this.dbManager = new DBManager(context);
        this.context = context;
    }

    public ArrayList<Card> getAll(){
        SQLiteDatabase db = dbManager.getDB();

        ArrayList<Card> cards = new ArrayList<Card>();

        CardPatternsRepository patternsRepository = new CardPatternsRepository(this.context);
        TransactionsRepository transactionsRepository = new TransactionsRepository(this.context);

        try{
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()){
                do{
                    Card card = new Card(
                        cursor.getInt(cursor.getColumnIndex(ColumnsNames.id)),
                        cursor.getString(cursor.getColumnIndex(ColumnsNames.code)),
                        cursor.getString(cursor.getColumnIndex(ColumnsNames.name)),
                        cursor.getFloat(cursor.getColumnIndex(ColumnsNames.balance))
                    );

                    card.cardPatterns = patternsRepository.getCardPatternsByCardId(card.id);
                    card.transactions = transactionsRepository.getTransactionsByCardId(card.id);

                    cards.add(card);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally{
            dbManager.close();
        }

        return cards;
    }

    public Card add(Card card){
        SQLiteDatabase db = dbManager.getDB();
        ContentValues values = new ContentValues();

        values.put(ColumnsNames.code, card.code);
        values.put(ColumnsNames.name, card.name);
        values.put(ColumnsNames.balance, card.balance);

        card.id = (int)db.insert(TABLE_NAME, null, values);

        dbManager.close();

        if (card.cardPatterns != null && card.cardPatterns.size() > 0){
            createCardPatterns(card);
        }

        if (card.transactions != null && card.transactions.size() > 0){
            createTransactions(card);
        }

        return card;
    }

    private void createCardPatterns(Card card){
        CardPatternsRepository patternsRepository = new CardPatternsRepository(this.context);
        for (CardPattern pattern : card.cardPatterns){
            pattern.cardId = card.id;
            patternsRepository.add(pattern);
        }
    }

    private void createTransactions(Card card){
        TransactionsRepository transactionsRepository = new TransactionsRepository(context);
        for (Transaction transaction : card.transactions){
            transaction.cardId = card.id;
            transactionsRepository.add(transaction);
        }
    }

    public void delete(int id){
        CardPatternsRepository patternsRepository = new CardPatternsRepository(this.context);
        TransactionsRepository transactionsRepository = new TransactionsRepository(this.context);

        patternsRepository.deleteAllByCardId(id);
        transactionsRepository.deleteAllByCardId(id);

        SQLiteDatabase db = dbManager.getDB();

        db.delete(TABLE_NAME, ColumnsNames.id + " = ?", new String[] {Integer.toString(id)});

        dbManager.close();
    }

    public void update(Card card){
        SQLiteDatabase db = dbManager.getDB();

        ContentValues values = new ContentValues();

        values.put(ColumnsNames.code, card.code);
        values.put(ColumnsNames.name, card.name);
        values.put(ColumnsNames.balance, card.balance);

        db.update(TABLE_NAME,values,ColumnsNames.id + " = ?", new String[] {Integer.toString(card.id)});

        dbManager.close();
    }
}

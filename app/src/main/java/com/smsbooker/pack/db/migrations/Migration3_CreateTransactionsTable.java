package com.smsbooker.pack.db.migrations;

import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.IMigration;
import com.smsbooker.pack.repositories.CardsRepository;
import com.smsbooker.pack.repositories.TransactionsRepository;

/**
 * Created by Yuriy on 14.05.2014.
 */
public class Migration3_CreateTransactionsTable implements IMigration {
    @Override
    public void update(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TransactionsRepository.TABLE_NAME + " (" +
                TransactionsRepository.ColumnsNames.id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TransactionsRepository.ColumnsNames.cardId + " INTEGER REFERENCES " +
                    CardsRepository.TABLE_NAME + "(" + CardsRepository.ColumnsNames.id + ")," +
                TransactionsRepository.ColumnsNames.type + " TEXT," +
                TransactionsRepository.ColumnsNames.value + " FLOAT," +
                TransactionsRepository.ColumnsNames.balance + " FLOAT," +
                TransactionsRepository.ColumnsNames.createdOn + " BIGINT," +
                TransactionsRepository.ColumnsNames.message + " TEXT )");
    }
}

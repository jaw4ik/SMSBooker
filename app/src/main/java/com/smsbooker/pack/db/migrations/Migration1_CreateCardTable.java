package com.smsbooker.pack.db.migrations;

import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.IMigration;

/**
 * Created by Yuriy on 04.05.2014.
 */
public class Migration1_CreateCardTable implements IMigration {
    @Override
    public void update(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cards (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code TEXT," +
                "name TEXT," +
                "phoneAddress TEXT," +
                "balance FLOAT," +
                "previousPattern TEXT," +
                "nextPattern TEXT )");
    }
}

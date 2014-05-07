package com.smsbooker.pack.db.migrations;

import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.IMigration;

/**
 * Created by Yuriy on 04.05.2014.
 */
public class Migration1_CreateCardTable implements IMigration {
    @Override
    public void update(SQLiteDatabase db) {
        db.execSQL("create table cards (" +
                "id integer primary key autoincrement," +
                "code text," +
                "name text," +
                "phoneAddress text," +
                "balance float )");
    }
}

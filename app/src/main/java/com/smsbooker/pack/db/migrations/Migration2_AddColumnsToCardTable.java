package com.smsbooker.pack.db.migrations;

import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.IMigration;

/**
 * Created by Yuriy on 14.05.2014.
 */
public class Migration2_AddColumnsToCardTable implements IMigration {
    @Override
    public void update(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE cards ADD COLUMN" +
                "previousPattern TEXT");
        db.execSQL("ALTER TABLE cards ADD COLUMN" +
                "nextPattern TEXT");
    }
}

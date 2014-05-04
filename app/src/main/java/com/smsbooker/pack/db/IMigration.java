package com.smsbooker.pack.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Yuriy on 04.05.2014.
 */
public interface IMigration {
    public void update(SQLiteDatabase db);
}

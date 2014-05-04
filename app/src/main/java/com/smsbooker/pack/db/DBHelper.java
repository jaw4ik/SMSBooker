package com.smsbooker.pack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yuriy on 04.05.2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    private DBManager dbManager;

    public DBHelper(Context context, String name, int version, DBManager manager) {
        super(context, name, null, version);
        dbManager = manager;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dbManager.updateDB(db, 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dbManager.updateDB(db, oldVersion);
    }
}

package com.smsbooker.pack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Yuriy on 03.05.2014.
 */

public class DBManager {

    final String DB_NAME = "SMSBookerDB";

    private DBHelper dbHelper;
    private MigrationsManager migrationsManager;

    private SQLiteDatabase db;

    public DBManager(Context context) {
        migrationsManager = new MigrationsManager();
        int dbVersion = migrationsManager.init();

        dbHelper = new DBHelper(context, DB_NAME, dbVersion, this);
    }

    public SQLiteDatabase getDB(){
        if (db == null){
            db = dbHelper.getWritableDatabase();
        }
        return db;
    }

    public void close(){
        if (db != null) {
            db.close();
            db = null;
        }
    }

    public void updateDB(SQLiteDatabase db, int fromVersion){
        migrationsManager.update(db, fromVersion);
    }
}

package com.smsbooker.pack.db;

import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.migrations.Migration1_CreateCardTable;
import com.smsbooker.pack.db.migrations.Migration2_AddColumnsToCardTable;

import java.util.ArrayList;

/**
 * Created by Yuriy on 03.05.2014.
 */
public class MigrationsManager {

    private ArrayList<IMigration> migrationsList = new ArrayList<IMigration>();

    public int init(){
        addMigration(new Migration1_CreateCardTable());
        //addMigration(new Migration2_AddColumnsToCardTable());

        return migrationsList.size();
    }

    public void update(SQLiteDatabase db, int fromVersion){
        for (int i = fromVersion; i < migrationsList.size(); i++){
            migrationsList.get(i).update(db);
        }
    }

    private void addMigration(IMigration migration){
        migrationsList.add(migration);
    }
}

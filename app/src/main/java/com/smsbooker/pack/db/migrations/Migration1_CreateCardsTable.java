package com.smsbooker.pack.db.migrations;

import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.IMigration;
import com.smsbooker.pack.repositories.CardsRepository;

/**
 * Created by Yuriy on 04.05.2014.
 */
public class Migration1_CreateCardsTable implements IMigration {

    @Override
    public void update(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CardsRepository.TABLE_NAME + " (" +
                CardsRepository.ColumnsNames.id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CardsRepository.ColumnsNames.code + " TEXT," +
                CardsRepository.ColumnsNames.name + " TEXT )");
    }
}

package com.smsbooker.pack.db.migrations;

import android.database.sqlite.SQLiteDatabase;

import com.smsbooker.pack.db.IMigration;
import com.smsbooker.pack.repositories.CardPatternsRepository;
import com.smsbooker.pack.repositories.CardsRepository;

/**
 * Created by Yuriy on 28.05.2014.
 */
public class Migration2_CreateCardPatternsTable implements IMigration {
    @Override
    public void update(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CardPatternsRepository.TABLE_NAME + " (" +
                CardPatternsRepository.ColumnsNames.id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CardPatternsRepository.ColumnsNames.cardId + " INTEGER REFERENCES "
                    + CardsRepository.TABLE_NAME + "(" + CardsRepository.ColumnsNames.id + ")," +
                CardPatternsRepository.ColumnsNames.address + " TEXT," +
                CardPatternsRepository.ColumnsNames.transactionType + " TEXT," +
                CardPatternsRepository.ColumnsNames.checkWord + " TEXT," +
                CardPatternsRepository.ColumnsNames.quantityPreviousValue + " TEXT," +
                CardPatternsRepository.ColumnsNames.quantityNextValue + " TEXT," +
                CardPatternsRepository.ColumnsNames.balancePreviousValue + " TEXT," +
                CardPatternsRepository.ColumnsNames.balanceNextValue + " TEXT )");
    }
}

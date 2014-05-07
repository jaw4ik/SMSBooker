package com.smsbooker.pack.messages;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.smsbooker.pack.models.Message;

import java.util.ArrayList;

/**
 * Created by Yuriy on 04.05.2014.
 */
public class MessagesManager {

    final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
    final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(SMS_CONTENT_URI, "inbox");

    final String[] projection = new String[] { "_id", "thread_id", "address", "date", "body", "read" };
    final String sortOrder = "date";

    Context context;

    public MessagesManager(Context context){
        this.context = context;
    }

    public ArrayList<Message> ReadMessages(){
        Cursor cursor = context.getContentResolver().query(SMS_INBOX_CONTENT_URI, projection, null, null, sortOrder);

        if (!cursor.moveToFirst()){
            cursor.close();
            return null;
        }

        ArrayList<Message> messages = new ArrayList<Message>();

        do{
            Message message = new Message(
                cursor.getLong(cursor.getColumnIndex("_id")),
                cursor.getLong(cursor.getColumnIndex("thread_id")),
                cursor.getString(cursor.getColumnIndex("address")),
                cursor.getString(cursor.getColumnIndex("body")),
                cursor.getLong(cursor.getColumnIndex("date")),
                cursor.getString(cursor.getColumnIndex("read")).equalsIgnoreCase("1")
            );
            messages.add(message);
        } while (cursor.moveToNext());

        cursor.close();

        return messages;
    }
}

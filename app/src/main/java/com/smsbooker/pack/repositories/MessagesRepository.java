package com.smsbooker.pack.repositories;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.smsbooker.pack.models.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 07.05.2014.
 */
public class MessagesRepository {

    private static class VarNames{
        public static String id = "_id";
        public static String threadId = "thread_id";
        public static String address = "address";
        public static String date = "date";
        public static String body = "body";
        public static String isRead = "read";
    }

    final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
    final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(SMS_CONTENT_URI, "inbox");

    final String[] projection = new String[] { VarNames.id, VarNames.threadId, VarNames.address, VarNames.date, VarNames.body, VarNames.isRead };
    final String sortOrder = "date";

    Context context;

    public MessagesRepository(Context context) {
        this.context = context;
    }

    private ArrayList<Message> getFromCursor(Cursor cursor){
        if (!cursor.moveToFirst()){
            cursor.close();
            return null;
        }

        ArrayList<Message> messages = new ArrayList<Message>();

        do{
            Message message = new Message(
                cursor.getLong(cursor.getColumnIndex(VarNames.id)),
                cursor.getLong(cursor.getColumnIndex(VarNames.threadId)),
                cursor.getString(cursor.getColumnIndex(VarNames.address)),
                cursor.getString(cursor.getColumnIndex(VarNames.body)),
                cursor.getLong(cursor.getColumnIndex(VarNames.date)),
                cursor.getString(cursor.getColumnIndex(VarNames.isRead)).equalsIgnoreCase("1")
            );
            messages.add(message);
        } while (cursor.moveToNext());

        cursor.close();

        return messages;
    }

    public ArrayList<Message> getAll(){
        Cursor cursor = context.getContentResolver().query(SMS_INBOX_CONTENT_URI, projection, null, null, sortOrder);

        return getFromCursor(cursor);
    }

    public ArrayList<Message> getChains(){
        ArrayList<String> messagesChainAddresses = new ArrayList<String>();
        ArrayList<Message> messagesChain = new ArrayList<Message>();

        ArrayList<Message> allMessages = getAll();
        Collections.reverse(allMessages);
        for (Message message : allMessages){
            if (!messagesChainAddresses.contains(message.fromAddress)){
                messagesChain.add(message);
                messagesChainAddresses.add(message.fromAddress);
            }
        }

        return messagesChain;
    }
}

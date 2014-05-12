package com.smsbooker.pack.repositories;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.smsbooker.pack.models.Message;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Yuriy on 07.05.2014.
 */
public class MessagesRepository {

    private static class ColumnsNames {
        public static String id = "_id";
        public static String threadId = "thread_id";
        public static String address = "address";
        public static String date = "date";
        public static String body = "body";
        public static String isRead = "read";
    }

    final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
    final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(SMS_CONTENT_URI, "inbox");

    final String[] projection = new String[] { ColumnsNames.id, ColumnsNames.threadId, ColumnsNames.address, ColumnsNames.date, ColumnsNames.body, ColumnsNames.isRead };
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
                cursor.getLong(cursor.getColumnIndex(ColumnsNames.id)),
                cursor.getLong(cursor.getColumnIndex(ColumnsNames.threadId)),
                cursor.getString(cursor.getColumnIndex(ColumnsNames.address)),
                cursor.getString(cursor.getColumnIndex(ColumnsNames.body)),
                cursor.getLong(cursor.getColumnIndex(ColumnsNames.date)),
                cursor.getString(cursor.getColumnIndex(ColumnsNames.isRead)).equalsIgnoreCase("1")
            );
            messages.add(message);
        } while (cursor.moveToNext());

        cursor.close();

        Collections.reverse(messages);

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
        for (Message message : allMessages){
            if (!messagesChainAddresses.contains(message.fromAddress)){
                messagesChain.add(message);
                messagesChainAddresses.add(message.fromAddress);
            }
        }

        return messagesChain;
    }

    public ArrayList<Message> getMessagesByAddress(String address){
        Cursor cursor = context.getContentResolver().query(SMS_INBOX_CONTENT_URI, projection, ColumnsNames.address + " = ?", new String[] { address }, sortOrder);

        return getFromCursor(cursor);
    }
}

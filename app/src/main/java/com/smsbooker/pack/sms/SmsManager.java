package com.smsbooker.pack.sms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Yuriy on 04.05.2014.
 */
public class SmsManager {

    final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
    final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(SMS_CONTENT_URI, "inbox");

    final String[] projection = new String[] { "_id", "thread_id", "address", "date", "body", "read" };
    String selection = "read in (0,1) and address = '10060'";
    final String sortOrder = "date";

    Context context;

    public SmsManager(Context context){
        this.context = context;
    }

    public void ReadMessages(){
        Cursor cursor = context.getContentResolver().query(SMS_INBOX_CONTENT_URI, projection, selection, null, sortOrder);

        if (!cursor.moveToFirst()){
            cursor.close();
            return;
        }

        do{
            Long messageId = cursor.getLong(0);
            Long threadId = cursor.getLong(1);
            String fromAddress = cursor.getString(2);
            Long timestamp = cursor.getLong(3);
            String messageBody = cursor.getString(4);
            Boolean isRead = cursor.getString(5).equalsIgnoreCase("1");
        } while (cursor.moveToNext());

        cursor.close();
    }
}

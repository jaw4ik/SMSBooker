package com.smsbooker.pack.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.smsbooker.pack.services.InboxMessagesHandleService;

import java.util.ArrayList;

/**
 * Created by Yuriy on 22.05.2014.
 */
public class SMSReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null || !intent.getAction().equalsIgnoreCase(ACTION)) {
            return;
        }

        Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
        ArrayList<SmsMessage> messages = createMessages(pduArray);

        Intent mIntent = new Intent(context, InboxMessagesHandleService.class);
        mIntent.putExtra("sms_address", getMessagesAddress(messages));
        mIntent.putExtra("sms_body", getMessagesBody(messages));

        context.startService(mIntent);
    }

    private ArrayList<SmsMessage> createMessages(Object[] pduArray){
        ArrayList<SmsMessage> messages = new ArrayList<SmsMessage>();
        for (Object pdu : pduArray){
            messages.add(SmsMessage.createFromPdu((byte[])pdu));
        }

        return messages;
    }

    private String getMessagesAddress(ArrayList<SmsMessage> messages){
        return messages.get(0).getDisplayOriginatingAddress();
    }

    private String getMessagesBody(ArrayList<SmsMessage> messages){
        StringBuilder bodyText = new StringBuilder();
        for (SmsMessage message : messages) {
            bodyText.append(message.getMessageBody());
        }

        return bodyText.toString();
    }
}

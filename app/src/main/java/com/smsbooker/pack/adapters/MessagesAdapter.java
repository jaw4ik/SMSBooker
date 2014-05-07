package com.smsbooker.pack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Message;

import java.util.ArrayList;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {
    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        super(context, R.layout.message_item, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout messageView;

        if (convertView == null){
            messageView = new LinearLayout(getContext());
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(R.layout.message_item, messageView, true);
        } else {
            messageView = (LinearLayout)convertView;
        }

        Message message = getItem(position);
        if (message == null){
            return null;
        }

        ((TextView)messageView.findViewById(R.id.tvPhoneAddress)).setText(message.fromAddress);
        ((TextView)messageView.findViewById(R.id.tvMessageBody)).setText(message.messageBody);

        return messageView;
    }
}

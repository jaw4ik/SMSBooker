package com.smsbooker.pack.messages;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.smsbooker.pack.models.Message;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {
    public MessagesAdapter(Context context, int resource) {
        super(context, resource);
    }


}

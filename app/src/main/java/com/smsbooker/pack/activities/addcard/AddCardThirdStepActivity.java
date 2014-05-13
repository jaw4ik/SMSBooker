package com.smsbooker.pack.activities.addcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smsbooker.pack.MessagesParser;
import com.smsbooker.pack.R;
import com.smsbooker.pack.models.MessagePart;

import java.util.ArrayList;

/**
 * Created by Yuriy on 12.05.2014.
 */
public class AddCardThirdStepActivity extends Activity {

    LinearLayout llParsedMessageBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_third_step);

        initControls();
        parseMessage();
    }

    private void parseMessage() {
        Intent intent = getIntent();
        if (intent == null){
            return;
        }

        String messageBody = intent.getStringExtra("message_body");

        MessagesParser parser = new MessagesParser();
        ArrayList<MessagePart> messageParts = parser.parseMessage(messageBody);

        for (MessagePart part : messageParts){
            TextView textView = new TextView(this);
            if (part.isNumber){

            }
        }
    }

    private void initControls() {
        llParsedMessageBody = (LinearLayout)findViewById(R.id.llParsedMessageBody);
    }
}

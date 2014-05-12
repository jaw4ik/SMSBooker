package com.smsbooker.pack.activities.addcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.smsbooker.pack.MessagesParser;
import com.smsbooker.pack.R;

/**
 * Created by Yuriy on 12.05.2014.
 */
public class AddCardThirdStepActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_third_step);

        Intent intent = getIntent();
        String messageBody = intent.getStringExtra("message_body");

        MessagesParser parser = new MessagesParser();
        parser.parseMessage(messageBody);

        initControls();
    }

    private void initControls() {

    }
}

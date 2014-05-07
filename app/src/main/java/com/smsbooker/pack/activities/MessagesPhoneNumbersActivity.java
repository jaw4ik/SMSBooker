package com.smsbooker.pack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.adapters.MessagesAdapter;
import com.smsbooker.pack.models.Message;
import com.smsbooker.pack.repositories.MessagesRepository;

import java.util.ArrayList;

/**
 * Created by Yuriy on 07.05.2014.
 */
public class MessagesPhoneNumbersActivity extends Activity implements AdapterView.OnItemClickListener {

    ArrayList<Message> messageChains;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_numbers);

        initControls();
    }

    private void initControls(){
        MessagesRepository messagesRepository = new MessagesRepository(this);

        ListView lvMessagesChains = (ListView)findViewById(R.id.lvMessagesChains);

        messageChains = messagesRepository.getChains();
        MessagesAdapter adapter = new MessagesAdapter(this, messageChains);
        lvMessagesChains.setAdapter(adapter);

        lvMessagesChains.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("address", messageChains.get(position).fromAddress);

        setResult(RESULT_OK, intent);
        finish();
    }
}

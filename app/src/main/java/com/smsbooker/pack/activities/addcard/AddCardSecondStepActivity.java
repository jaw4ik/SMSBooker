package com.smsbooker.pack.activities.addcard;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Message;
import com.smsbooker.pack.repositories.MessagesRepository;

import java.util.ArrayList;

/**
 * Created by Yuriy on 11.05.2014.
 */
public class AddCardSecondStepActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView tvMessages;

    ArrayAdapter<Message> adapter;
    ArrayList<Message> messagesList = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_second_step);

        initControls();
        fillMessagesList();
    }

    private void initControls(){
        adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messagesList);
        tvMessages = (ListView)findViewById(R.id.tvMessages);
        tvMessages.setAdapter(adapter);
        tvMessages.setOnItemClickListener(this);
    }

    private void fillMessagesList(){
        Intent intent = getIntent();
        if (intent == null){
            finishWithCancelledResult();
            return;
        }

        String address = intent.getStringExtra("address");
        if (address == null){
            finishWithCancelledResult();
            return;
        }

        MessagesRepository messagesRepository = new MessagesRepository(this);
        ArrayList<Message> messages = messagesRepository.getMessagesByAddress(address);

        if (messages == null || messages.size() == 0){
            finishWithCancelledResult();
        }

        updateMessagesList(messages);
    }

    private void updateMessagesList(ArrayList<Message> messages){
        messagesList.clear();
        messagesList.addAll(messages);
        adapter.notifyDataSetChanged();
    }

    private void finishWithCancelledResult(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AddCardThirdStepActivity.class);
        intent.putExtra("message_body", messagesList.get(position).messageBody);

        Bundle animationBundle = ActivityOptions.makeCustomAnimation(this, R.anim.slide_left_in, R.anim.slide_left_out).toBundle();
        startActivityForResult(intent, 0, animationBundle);
    }
}

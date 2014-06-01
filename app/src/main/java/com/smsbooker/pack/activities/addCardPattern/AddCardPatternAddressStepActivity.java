package com.smsbooker.pack.activities.addCardPattern;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.adapters.MessagesAdapter;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Message;
import com.smsbooker.pack.repositories.MessagesRepository;

import java.util.ArrayList;

/**
 * Created by Yuriy on 07.05.2014.
 */
public class AddCardPatternAddressStepActivity extends Activity implements AdapterView.OnItemClickListener {

    ArrayList<Message> messageChains;

    String selectedAddress;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_pattern_address_step);

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
        selectedAddress = messageChains.get(position).fromAddress;

        Intent intent = new Intent(this, AddCardPatternMessageStepActivity.class);
        intent.putExtra("address", selectedAddress);

        Bundle animationBundle = ActivityOptions.makeCustomAnimation(this, R.anim.slide_left_in, R.anim.slide_left_out).toBundle();
        startActivityForResult(intent, 0, animationBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }

        CardPattern cardPattern = data.getParcelableExtra(CardPattern.class.getCanonicalName());
        cardPattern.address = selectedAddress;
        data.putExtra(CardPattern.class.getCanonicalName(), cardPattern);

        setResult(RESULT_OK, data);
        finish();
    }
}

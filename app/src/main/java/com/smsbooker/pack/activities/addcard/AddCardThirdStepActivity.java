package com.smsbooker.pack.activities.addcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smsbooker.pack.MessagesParser;
import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.models.MessagePart;
import com.smsbooker.pack.viewcontrols.FlowLayout;

import java.util.ArrayList;

/**
 * Created by Yuriy on 12.05.2014.
 */
public class AddCardThirdStepActivity extends Activity implements View.OnClickListener {

    FlowLayout flParsedMessageBody;
    EditText etBalance;
    Button btnFinish;

    MessagesParser parser = new MessagesParser();

    ArrayList<MessagePart> messageParts;
    int selectedPartIndex = -1;

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

        messageParts = parser.parseMessage(messageBody);

        for (int i = 0; i < messageParts.size(); i++){
            MessagePart part = messageParts.get(i);
            TextView textView = new TextView(this);
            if (part.isNumber){
                textView.setId(i);
                textView.setOnClickListener(this);
                setNumberButtonStyles(textView);
            } else {
                setTextStyles(textView);
            }
            textView.setText(part.value);
            flParsedMessageBody.addView(textView);
        }
    }

    private void setNumberButtonStyles(TextView textView){
        textView.setTextAppearance(this, R.style.messagePartButton);
        textView.setBackgroundColor(Color.parseColor("#ff0099cc"));
    }

    private void setTextStyles(TextView textView){
        textView.setTextAppearance(this, R.style.messagePartText);
    }

    private void initControls() {
        flParsedMessageBody = (FlowLayout)findViewById(R.id.flParsedMessageBody);
        etBalance = (EditText)findViewById(R.id.etBalance);
        btnFinish = (Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int selectedViewId = v.getId();
        switch (selectedViewId){
            case R.id.btnFinish:
                finishCreating();
                break;
            default:
                valueSelected(selectedViewId);
                break;
        }
    }

    private void valueSelected(int id){
        selectedPartIndex = id;
        MessagePart selectedPart = messageParts.get(selectedPartIndex);
        if (selectedPart == null){
            return;
        }

        try{
            Float selectedValue = Float.parseFloat(selectedPart.value.replace(',', '.'));
            etBalance.setText(Float.toString(selectedValue));
        } catch (Exception e){
            Toast.makeText(this, R.string.invalid_value, Toast.LENGTH_SHORT).show();
        }
    }

    private void finishCreating(){
        if (etBalance.getText().length() == 0 || selectedPartIndex == -1){
            Toast.makeText(this, R.string.invalid_value_selected_validation_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        Card newCard = new Card();
        newCard.pattern.previousText = parser.getPreviousPartText(messageParts, selectedPartIndex);
        newCard.pattern.nextText = parser.getNextPartText(messageParts, selectedPartIndex);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("card", newCard.toBundle());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

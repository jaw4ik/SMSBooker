package com.smsbooker.pack.activities.addCardPattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smsbooker.pack.MessageParser;
import com.smsbooker.pack.R;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.MessagePart;
import com.smsbooker.pack.models.ValuePattern;
import com.smsbooker.pack.viewControls.FlowLayout;

import java.util.ArrayList;

/**
 * Created by Yuriy on 12.05.2014.
 */
public class AddCardPatternBalanceStepActivity extends Activity implements View.OnClickListener {

    FlowLayout flParsedMessageBody;
    EditText etBalance;
    Button btnFinish;

    MessageParser parser;

    ArrayList<MessagePart> messageParts;
    int selectedPartIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_pattern_balance_step);

        initControls();
        parseMessage();
    }

    private void parseMessage() {
        Intent intent = getIntent();
        if (intent == null){
            return;
        }

        parser = new MessageParser(intent.getStringExtra("message_body"));
        messageParts = parser.getMessageParts();

        for (int i = 0; i < messageParts.size(); i++){
            MessagePart part = messageParts.get(i);

            TextView textView = new TextView(this);
            if (part.isNumber){
                textView.setId(i);
                setClickableButtonStyles(textView);
            } else {
                setTextStyles(textView);
            }
            textView.setText(part.value);

            flParsedMessageBody.addView(textView);
        }
    }

    private void setClickableButtonStyles(TextView textView){
        textView.setOnClickListener(this);
        textView.setTextAppearance(this, R.style.messagePartButton);
        setUnselectedColor(textView);
    }

    private void setUnselectedColor(TextView textView){
        textView.setBackgroundColor(Color.parseColor("#ff0099cc"));
    }

    private void setSelectedColor(TextView textView){
        textView.setBackgroundColor(Color.parseColor("#ff669900"));
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

            setSelectedValueStyle(id);
        } catch (Exception e){
            Toast.makeText(this, R.string.invalid_value, Toast.LENGTH_SHORT).show();
        }
    }

    private void setSelectedValueStyle(int selectedViewId){
        int partsCount = messageParts.size();
        for (int i = 0; i < partsCount; i++){
            TextView textView = (TextView)flParsedMessageBody.findViewById(i);
            if (textView == null) continue;

            setUnselectedColor(textView);
        }

        setSelectedColor((TextView)flParsedMessageBody.findViewById(selectedViewId));
    }

    private void finishCreating(){
        if (etBalance.getText().length() == 0 || selectedPartIndex == -1){
            Toast.makeText(this, R.string.invalid_value_selected_validation_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        CardPattern cardPattern = new CardPattern();
        cardPattern.balanceValuePattern = parser.getPattern(selectedPartIndex);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(CardPattern.class.getCanonicalName(), cardPattern);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

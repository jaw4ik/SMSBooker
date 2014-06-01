package com.smsbooker.pack.activities.addCardPattern;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smsbooker.pack.MessageParser;
import com.smsbooker.pack.R;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.MessagePart;
import com.smsbooker.pack.models.Transaction;
import com.smsbooker.pack.viewControls.FlowLayout;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Yuriy on 28.05.2014.
 */
public class AddCardPatternCheckwordStepActivity extends Activity implements View.OnClickListener {

    TextView tvDescription;
    FlowLayout flParsedMessageBody;
    Button btnContinue;

    MessageParser parser;
    ArrayList<MessagePart> messageParts;
    int selectedPartIndex = -1;
    String selectedCheckword;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_pattern_checkword_step);

        InitControls();

        intent = getIntent();
        if (intent == null){
            return;
        }

        setActivityDescription(intent);
        parseMessage(intent);
    }

    private void setActivityDescription(Intent intent) {
        Transaction.Type type = (Transaction.Type)intent.getSerializableExtra("transaction_type");
        if (type == null){
            return;
        }

        tvDescription.setText(type == Transaction.Type.increment
                ? R.string.select_increment_checkword_description
                : R.string.select_decrement_checkword_description);
    }

    private void InitControls() {
        tvDescription = (TextView)findViewById(R.id.tvDescription);
        flParsedMessageBody = (FlowLayout)findViewById(R.id.flParsedMessageBody);
        btnContinue = (Button)findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
    }

    private void parseMessage(Intent intent) {
        parser = new MessageParser(intent.getStringExtra("message_body"));
        messageParts = parser.getMessageParts();

        for (int i = 0; i < messageParts.size(); i++){
            MessagePart part = messageParts.get(i);
            TextView textView = new TextView(this);

            if (part.isNumber){
                setTextStyles(textView);
            } else {
                textView.setId(i);
                setClickableButtonStyles(textView);
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

    @Override
    public void onClick(View v) {
        int selectedViewId = v.getId();
        switch (selectedViewId){
            case R.id.btnContinue:
                continueCreating();
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

        setSelectedValueStyle(id);
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

    private void continueCreating(){
        MessagePart selectedPart = messageParts.get(selectedPartIndex);
        if (selectedPart == null){
            return;
        }

        if (selectedPart.value.length() <= 1){
            Toast.makeText(this, R.string.invalid_value, Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedPartIndex == -1){
            Toast.makeText(this, R.string.no_checkword_selected_validation_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        selectedCheckword = selectedPart.value;

        intent.setClass(this, AddCardPatternQuantityStepActivity.class);

        Bundle animationBundle = ActivityOptions.makeCustomAnimation(this, R.anim.slide_left_in, R.anim.slide_left_out).toBundle();
        startActivityForResult(intent, 0, animationBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }

        CardPattern cardPattern = data.getParcelableExtra(CardPattern.class.getCanonicalName());
        cardPattern.checkWord = selectedCheckword;
        data.putExtra(CardPattern.class.getCanonicalName(), cardPattern);

        setResult(RESULT_OK, data);
        finish();
    }
}

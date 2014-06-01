package com.smsbooker.pack.activities.addCardPattern;

import android.app.Activity;
import android.app.ActivityOptions;
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
 * Created by Yuriy on 30.05.2014.
 */
public class AddCardPatternQuantityStepActivity extends Activity implements View.OnClickListener {

    FlowLayout flParsedMessageBody;
    EditText etQuantity;
    Button btnContinue;

    MessageParser parser;

    ArrayList<MessagePart> messageParts;
    int selectedPartIndex = -1;

    Intent intent;
    ValuePattern quantityPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_pattern_quantity_step);

        initControls();
        parseMessage();
    }

    private void parseMessage() {
        intent = getIntent();
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
        etQuantity = (EditText)findViewById(R.id.etQuantity);
        btnContinue = (Button)findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
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

        try{
            Float selectedValue = Float.parseFloat(selectedPart.value.replace(',', '.'));
            etQuantity.setText(Float.toString(selectedValue));

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

    private void continueCreating(){
        if (etQuantity.getText().length() == 0 || selectedPartIndex == -1){
            Toast.makeText(this, R.string.invalid_value_selected_validation_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        quantityPattern = parser.getPattern(selectedPartIndex);

        intent.setClass(this, AddCardPatternBalanceStepActivity.class);

        Bundle animationBundle = ActivityOptions.makeCustomAnimation(this, R.anim.slide_left_in, R.anim.slide_left_out).toBundle();
        startActivityForResult(intent, 0, animationBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }

        CardPattern cardPattern = data.getParcelableExtra(CardPattern.class.getCanonicalName());
        cardPattern.quantityValuePattern = quantityPattern;
        data.putExtra(CardPattern.class.getCanonicalName(), cardPattern);

        setResult(RESULT_OK, data);
        finish();
    }
}

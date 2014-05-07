package com.smsbooker.pack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Card;

/**
 * Created by Yuriy on 07.05.2014.
 */
public class AddCardActivity extends Activity implements View.OnClickListener {

    EditText etTitle;
    EditText etPhoneNumber;
    EditText etBalance;
    Button btnCreateCard;
    Button btnFromMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        initControls();
    }

    private void initControls(){
        etTitle = (EditText)findViewById(R.id.etTitle);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        etBalance = (EditText)findViewById(R.id.etBalance);
        btnCreateCard = (Button)findViewById(R.id.btnCreateCard);
        btnCreateCard.setOnClickListener(this);
        btnFromMessages = (Button)findViewById(R.id.btnFromMessages);
        btnFromMessages.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateCard:
                createCard();
                break;
            case R.id.btnFromMessages:
                openFromMessagesDialog();
                break;
        }
    }

    private void createCard(){
        if (etTitle.length() == 0){
            Toast.makeText(this, R.string.title_card_validation_msg, Toast.LENGTH_LONG).show();
            return;
        }

        if (etPhoneNumber.length() == 0){
            Toast.makeText(this, R.string.phone_number_card_validation_msg, Toast.LENGTH_LONG).show();
            return;
        }

        Card card = new Card(etTitle.getText().toString(), etPhoneNumber.getText().toString(), Float.parseFloat(etBalance.getText().toString()));

        Intent intent = new Intent();
        intent.putExtra("card", card.toBundle());

        setResult(RESULT_OK, intent);
        finish();
    }

    private void openFromMessagesDialog(){
        Intent intent = new Intent(this, MessagesPhoneNumbersActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null){
            return;
        }

        if (resultCode != RESULT_OK){
            return;
        }

        etPhoneNumber.setText(data.getStringExtra("address"));
    }
}

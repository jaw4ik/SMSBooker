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
    }

    @Override
    public void onClick(View v) {
        if (etTitle.length() == 0){
            Toast.makeText(this, R.string.title_card_validation_msg, Toast.LENGTH_LONG);
            return;
        }

        if (etPhoneNumber.length() == 0){
            Toast.makeText(this, R.string.phone_number_card_validation_msg, Toast.LENGTH_LONG);
            return;
        }

        Card card = new Card(etTitle.getText().toString(), etPhoneNumber.getText().toString(), Float.parseFloat(etBalance.getText().toString()));

        Intent intent = new Intent();
        intent.putExtra("card", card.toBundle());

        setResult(RESULT_OK, intent);
        finish();
    }
}

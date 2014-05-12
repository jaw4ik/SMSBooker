package com.smsbooker.pack.activities.addcard;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smsbooker.pack.R;
import com.smsbooker.pack.activities.MessagesPhoneNumbersActivity;

/**
 * Created by Yuriy on 11.05.2014.
 */
public class AddCardFirstStepActivity extends Activity implements View.OnClickListener {

    final int OPEN_MESSAGES_DIALOG_RESULT_CODE = 0;
    final int SECOND_STEP_RESULT_CODE = 1;

    EditText etTitle;
    EditText etPhoneNumber;
    Button btnFromMessages;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_first_step);

        initControls();
    }

    private void initControls(){
        etTitle = (EditText)findViewById(R.id.etTitle);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        btnFromMessages = (Button)findViewById(R.id.btnFromMessages);
        btnFromMessages.setOnClickListener(this);
        btnContinue = (Button)findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
    }

    private void continueCreation(){
        if (etTitle.length() == 0){
            Toast.makeText(this, R.string.title_card_validation_msg, Toast.LENGTH_LONG).show();
            return;
        }

        if (etPhoneNumber.length() == 0){
            Toast.makeText(this, R.string.phone_number_card_validation_msg, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, AddCardSecondStepActivity.class);
        intent.putExtra("address", etPhoneNumber.getText().toString());

        Bundle animationBundle = ActivityOptions.makeCustomAnimation(this, R.anim.slide_left_in, R.anim.slide_left_out).toBundle();
        startActivityForResult(intent, SECOND_STEP_RESULT_CODE, animationBundle);
    }

    private void openFromMessagesDialog(){
        Intent intent = new Intent(this, MessagesPhoneNumbersActivity.class);
        startActivityForResult(intent, OPEN_MESSAGES_DIALOG_RESULT_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFromMessages:
                openFromMessagesDialog();
                break;
            case R.id.btnContinue:
                continueCreation();
                break;
        }
    }

    private void openMessagesDialogHandler(int resultCode, Intent data){
        if (data == null){
            return;
        }

        if (resultCode != RESULT_OK){
            return;
        }

        etPhoneNumber.setText(data.getStringExtra("address"));
    }

    private void secondStepHandler(int resultCode, Intent data){
        switch (resultCode){
            case RESULT_CANCELED:
                Toast.makeText(this, R.string.no_messages_validation_msg, Toast.LENGTH_LONG).show();
                break;
            case RESULT_OK:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case OPEN_MESSAGES_DIALOG_RESULT_CODE:
                openMessagesDialogHandler(resultCode, data);
                break;
            case SECOND_STEP_RESULT_CODE:
                secondStepHandler(resultCode, data);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

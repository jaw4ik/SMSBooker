package com.smsbooker.pack.activities.addCardPattern;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Transaction;

/**
 * Created by Yuriy on 28.05.2014.
 */
public class AddCardPatternTypeStepActivity extends Activity implements View.OnClickListener {

    TextView tvMessageBody;
    RadioGroup rgMessageType;
    Button btnContinue;

    Transaction.Type transactionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_pattern_type_step);

        InitControls();
        InitMessageBody();
    }

    private void InitControls() {
        tvMessageBody = (TextView)findViewById(R.id.tvMessageBody);
        rgMessageType = (RadioGroup)findViewById(R.id.rgMessageType);
        btnContinue = (Button)findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
    }

    private void InitMessageBody() {
        Intent intent = getIntent();
        tvMessageBody.setText(intent.getStringExtra("message_body"));
    }

    @Override
    public void onClick(View v) {
        int checkedRadioButtonId = rgMessageType.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1){
            Toast.makeText(this, R.string.no_value_checked_validation_msg, Toast.LENGTH_LONG).show();
            return;
        }


        switch (checkedRadioButtonId){
            case R.id.rbIncrement:
                transactionType = Transaction.Type.increment;
                break;
            case R.id.rbDecrement:
                transactionType = Transaction.Type.decrement;
                break;
        }

        Intent intent = getIntent();
        intent.setClass(this, AddCardPatternCheckwordStepActivity.class);
        intent.putExtra("transaction_type", transactionType);

        Bundle animationBundle = ActivityOptions.makeCustomAnimation(this, R.anim.slide_left_in, R.anim.slide_left_out).toBundle();
        startActivityForResult(intent, 0, animationBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }

        CardPattern cardPattern = data.getParcelableExtra(CardPattern.class.getCanonicalName());
        cardPattern.transactionType = transactionType;
        data.putExtra(CardPattern.class.getCanonicalName(), cardPattern);

        setResult(RESULT_OK, data);
        finish();
    }
}

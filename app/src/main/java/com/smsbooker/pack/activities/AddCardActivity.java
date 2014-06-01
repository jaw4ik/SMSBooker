package com.smsbooker.pack.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.smsbooker.pack.R;
import com.smsbooker.pack.activities.addCardPattern.AddCardPatternAddressStepActivity;
import com.smsbooker.pack.adapters.CardPatternsAdapter;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Transaction;
import com.smsbooker.pack.models.ValuePattern;

import java.util.ArrayList;

/**
 * Created by Yuriy on 11.05.2014.
 */
public class AddCardActivity extends Activity implements View.OnClickListener {

    EditText etTitle;
    Button btnAddCardPattern;
    Button btnFinish;
    ListView lvCardPatterns;

    ArrayList<CardPattern> cardPatterns;
    CardPatternsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        cardPatterns = new ArrayList<CardPattern>();
        adapter = new CardPatternsAdapter(this, cardPatterns);

        initControls();
    }

    private void initControls(){
        etTitle = (EditText)findViewById(R.id.etTitle);
        btnAddCardPattern = (Button)findViewById(R.id.btnAddCardPattern);
        btnAddCardPattern.setOnClickListener(this);
        btnFinish = (Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        lvCardPatterns = (ListView)findViewById(R.id.lvCardPatterns);
        lvCardPatterns.setAdapter(adapter);
        registerForContextMenu(lvCardPatterns);
    }

    private void openAddCardPatternActivity(){
        Intent intent = new Intent(this, AddCardPatternAddressStepActivity.class);

        Bundle animationBundle = ActivityOptions.makeCustomAnimation(this, R.anim.slide_left_in, R.anim.slide_left_out).toBundle();
        startActivityForResult(intent, 0, animationBundle);
    }

    private void finishCreating(){
        if (etTitle.length() == 0){
            Toast.makeText(this, R.string.title_card_validation_msg, Toast.LENGTH_LONG).show();
            return;
        }

        if (cardPatterns.size() == 0){
            Toast.makeText(this, R.string.empty_card_patterns_validation_msg, Toast.LENGTH_LONG).show();
            return;
        }

        Card card = new Card(etTitle.getText().toString(), cardPatterns);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(Card.class.getCanonicalName(), card);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(R.string.delete_pattern);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        deleteCardPattern(acmi.position);

        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddCardPattern:
                openAddCardPatternActivity();
                break;
            case R.id.btnFinish:
                finishCreating();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }

        CardPattern cardPattern = data.getParcelableExtra(CardPattern.class.getCanonicalName());
        addCardPattern(cardPattern);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void deleteCardPattern(int position) {
        cardPatterns.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void addCardPattern(CardPattern cardPattern) {
        cardPatterns.add(cardPattern);
        adapter.notifyDataSetChanged();
    }
}

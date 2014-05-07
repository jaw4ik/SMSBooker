package com.smsbooker.pack.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.adapters.CardsAdapter;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.repositories.CardsRepository;

import java.util.ArrayList;

public class CardsListActivity extends ActionBarActivity implements View.OnClickListener {

    ListView lvCards;
    CardsAdapter adapter;
    CardsRepository cardsRepository;

    Button btnAddCard;

    ArrayList<Card> cardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);

        btnAddCard = (Button)findViewById(R.id.btnAddCard);
        btnAddCard.setOnClickListener(this);

        lvCards = (ListView)findViewById(R.id.lvCards);
        cardsRepository = new CardsRepository(getBaseContext());

        cardsList = cardsRepository.getAll();
        adapter = new CardsAdapter(this, cardsList);
        lvCards.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cards_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddCard:
                Intent intent = new Intent(this, AddCardActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data == null){
            return;
        }

        if (resultCode != RESULT_OK){
            return;
        }

        Card card = new Card(data.getBundleExtra("card"));
        cardsRepository.add(card);
        cardsList.add(card);

        adapter.notifyDataSetChanged();
    }
}

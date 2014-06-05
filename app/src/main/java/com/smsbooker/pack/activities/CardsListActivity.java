package com.smsbooker.pack.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.adapters.CardsAdapter;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.repositories.CardsRepository;

import java.util.ArrayList;

public class CardsListActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    final int CM_DELETE_CARD = 1;
    final int CM_EDIT_CARD = 2;

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
        cardsRepository = new CardsRepository(this);

        cardsList = cardsRepository.getAll();
        adapter = new CardsAdapter(this, cardsList);
        lvCards.setAdapter(adapter);
        lvCards.setOnItemClickListener(this);

        registerForContextMenu(lvCards);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, CM_DELETE_CARD, 0, R.string.delete_card);
        menu.add(0, CM_EDIT_CARD, 0, R.string.edit_card);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case CM_DELETE_CARD:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                deleteCard(acmi.position);
                break;
           case CM_EDIT_CARD:
                acmi = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                updateCard(acmi.position);
                break;
        }

        return super.onContextItemSelected(item);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Card card = cardsList.get(position);

        Intent intent = new Intent(this, TransactionsListActivity.class);
        intent.putExtra(Card.class.getCanonicalName(), card);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data == null){
            return;
        }

        if (resultCode != RESULT_OK){
            return;
        }

        Card card = data.getParcelableExtra(Card.class.getCanonicalName());
        addCard(card);
    }

    private void addCard(Card card){
        card.createTransactions(this);

        cardsRepository.add(card);
        cardsList.add(card);

        adapter.notifyDataSetChanged();
    }

    private void deleteCard(int position){
        Card cardToDelete = cardsList.get(position);

        cardsRepository.delete(cardToDelete.id);
        cardsList.remove(position);

        adapter.notifyDataSetChanged();
    }

    private void updateCard(int position){
        Card card = cardsList.get(position);
        cardsRepository.update(card);
        cardsList.remove(card);
        cardsList.add(position, card);

        adapter.notifyDataSetChanged();
    }
}
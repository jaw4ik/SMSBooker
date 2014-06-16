package com.smsbooker.pack.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Transaction;

import java.util.ArrayList;

/**
 * Created by Yuriy on 14.06.2014.
 */
public class CardsListAdapter extends ArrayAdapter<Card> {

    ArrayList<Card> checkedCards = new ArrayList<Card>();

    public CardsListAdapter(Context context, ArrayList<Card> cards) {
        super(context, R.layout.widget_configure_card_item, cards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout cardView;

        if (convertView == null){
            cardView = new LinearLayout(getContext());
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(R.layout.widget_configure_card_item, cardView, true);
        } else {
            cardView = (LinearLayout)convertView;
        }

        Card card = getItem(position);
        if (card == null){
            return null;
        }

        Switch switchCardItem = (Switch)cardView.findViewById(R.id.switchCardItem);
        switchCardItem.setText(card.name);

        switchCardItem.setTag(card);
        switchCardItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Card card = (Card)buttonView.getTag();
                if (card == null){
                    return;
                }

                if (isChecked){
                    checkedCards.add(card);
                } else {
                    checkedCards.remove(card);
                }
            }
        });

        return cardView;
    }

    public ArrayList<Card> getCheckedCardsList(){
        return this.checkedCards;
    }
}

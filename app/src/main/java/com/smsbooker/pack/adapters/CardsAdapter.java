package com.smsbooker.pack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 06.05.2014.
 */
public class CardsAdapter extends ArrayAdapter<Card> {

    public CardsAdapter(Context context, ArrayList<Card> cards) {
        super(context, R.layout.card_item, cards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LinearLayout cardView;

        if (convertView == null){
            cardView = new LinearLayout(getContext());
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(R.layout.card_item, cardView, true);
        } else {
            cardView = (LinearLayout)convertView;
        }

        Card card = getItem(position);
        if (card == null){
            return null;
        }

        ((TextView)cardView.findViewById(R.id.tvName)).setText(card.name);
        ((TextView)cardView.findViewById(R.id.tvPhoneAddress)).setText(card.phoneAddress);
        ((TextView)cardView.findViewById(R.id.tvBalance)).setText(Float.toString(card.balance));

        return cardView;
    }
}
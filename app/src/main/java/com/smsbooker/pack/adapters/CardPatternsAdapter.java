package com.smsbooker.pack.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Transaction;

import java.util.ArrayList;

/**
 * Created by Yuriy on 30.05.2014.
 */
public class CardPatternsAdapter extends ArrayAdapter<CardPattern> {

    public CardPatternsAdapter(Context context, ArrayList<CardPattern> cardPatterns) {
        super(context, R.layout.card_pattern_item, cardPatterns);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout cardView;

        if (convertView == null){
            cardView = new LinearLayout(getContext());
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(R.layout.card_pattern_item, cardView, true);
        } else {
            cardView = (LinearLayout)convertView;
        }

        CardPattern cardPattern = getItem(position);
        if (cardPattern == null){
            return null;
        }

        if (cardPattern.transactionType == Transaction.Type.increment) {
            cardView.setBackgroundColor(Color.parseColor("#ff669900"));
        } else {
            cardView.setBackgroundColor(Color.parseColor("#ffff4444"));
        }

        ((TextView)cardView.findViewById(R.id.tvNumber)).setText(Integer.toString(position + 1) + ".");
        ((TextView)cardView.findViewById(R.id.tvAddress)).setText(cardPattern.address);
        ((TextView)cardView.findViewById(R.id.tvCheckword)).setText(cardPattern.checkWord);

        return cardView;
    }
}

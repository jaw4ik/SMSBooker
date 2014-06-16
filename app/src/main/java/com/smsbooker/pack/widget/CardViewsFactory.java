package com.smsbooker.pack.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.smsbooker.pack.R;
import com.smsbooker.pack.activities.TransactionsListActivity;
import com.smsbooker.pack.models.Card;

import java.util.ArrayList;

/**
 * Created by Yuriy on 11.06.2014.
 */
public class CardViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    int appWidgetId;
    ArrayList<Card> cardsList;

    public CardViewsFactory(Context context, Intent intent, ArrayList<Card> cards) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.cardsList = cards;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cardsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Card card = cardsList.get(position);
        if (card == null){
            return null;
        }

        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.app_widget_card_item);

        row.setTextViewText(R.id.tvName, card.name);
        row.setTextViewText(R.id.tvBalance, String.format("%.2f", card.getBalance()));

        Intent intent = new Intent();
        intent.putExtra(Card.class.getCanonicalName(), card);

        row.setOnClickFillInIntent(R.id.llCardItem, intent);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

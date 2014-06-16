package com.smsbooker.pack.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.repositories.CardsRepository;

import java.util.ArrayList;

/**
 * Created by Yuriy on 11.06.2014.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Context applicationContext = this.getApplicationContext();

        CardsRepository cardsRepository = new CardsRepository(applicationContext);

        ArrayList<Integer> cardsIds = intent.getIntegerArrayListExtra(WidgetProvider.WIDGET_CARDS_IDS);
        ArrayList<Card> cardsList = new ArrayList<Card>();
        for (int id : cardsIds){
            cardsList.add(cardsRepository.getCardById(id));
        }

        return new CardViewsFactory(applicationContext, intent, cardsList);
    }
}

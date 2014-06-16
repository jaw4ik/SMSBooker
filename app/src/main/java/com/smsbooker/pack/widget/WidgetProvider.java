package com.smsbooker.pack.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import com.smsbooker.pack.R;
import com.smsbooker.pack.activities.CardsListActivity;
import com.smsbooker.pack.activities.TransactionsListActivity;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetConfigureActivity AppWidgetConfigureActivity}
 */
public class WidgetProvider extends AppWidgetProvider {

    final static String WIDGET_CARDS_IDS = "selected_cards_ids";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            WidgetConfigureActivity.deletePreferences(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        ArrayList<Integer> cardsIds = WidgetConfigureActivity.getPreferences(context, appWidgetId);
        if (cardsIds.size() == 0){
            return;
        }

        //Cards list
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.putExtra(WIDGET_CARDS_IDS, cardsIds);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.listView, serviceIntent);

        Intent clickIntent = new Intent(context, TransactionsListActivity.class);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listView, clickPendingIntent);

        //Open application button
        Intent openAppIntent = new Intent(context, CardsListActivity.class);
        views.setOnClickPendingIntent(R.id.btnOpenApplication, PendingIntent.getActivity(context, 1, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}



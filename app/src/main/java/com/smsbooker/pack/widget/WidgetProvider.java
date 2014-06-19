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

    // Константа-ключ для сохранения настроек
    final static String WIDGET_CARDS_IDS = "selected_cards_ids";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Обновление всех виджетов данного приложения, добавленых на экраны
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            // Обновление каждого виджета
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Удаление настроек, когда пользователь удаляет виджеты
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            WidgetConfigureActivity.deletePreferences(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Обьект с настройками для элементов виджета
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        // Получение настроек пользователя (списка карт, необходимых для отображения на виджете)
        ArrayList<Integer> cardsIds = WidgetConfigureActivity.getPreferences(context, appWidgetId);
        if (cardsIds.size() == 0){
            return;
        }

        // Настройка списка карт, WidgetService - клас, отвечающий за построение списка
        Intent serviceIntent = new Intent(context, WidgetService.class);
        // Указываем идентификатор виджета, который будем обновлять
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // Указываем список карт, которые будем отображать в списке
        serviceIntent.putExtra(WIDGET_CARDS_IDS, cardsIds);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // Применение настроек списка с элементу виджета
        views.setRemoteAdapter(R.id.listView, serviceIntent);

        // Настройка события при нажатии на карту (открывание списка транзакций по карте)
        Intent clickIntent = new Intent(context, TransactionsListActivity.class);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Применение события к списку карт
        views.setPendingIntentTemplate(R.id.listView, clickPendingIntent);

        // Настройка события при нажатии на кнопку "В приложение" (открывание приложения)
        Intent openAppIntent = new Intent(context, CardsListActivity.class);
        // Применение события к кнопке
        views.setOnClickPendingIntent(R.id.btnOpenApplication, PendingIntent.getActivity(context, 1, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        // Обновление виджета
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}



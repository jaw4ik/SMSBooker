package com.smsbooker.pack.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.repositories.CardsRepository;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link WidgetProvider AppWidget} AppWidget.
 */
public class WidgetConfigureActivity extends Activity {

    // Ключи для сохранения настроек
    private static final String PREFS_NAME = "com.smsbooker.pack.widgets.AppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    // Идентификатор текущего виджета
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    // Контрол списка
    ListView lvCardsList;
    // Адаптер для отображения списка
    CardsListAdapter adapter;

    public WidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Установка результата по умолчанию CANCELED для того, чтобы
        // виджет не создался если пользователь вийдет из экрана настроек
        setResult(RESULT_CANCELED);
        // Устанивка вида для текущего активити
        setContentView(R.layout.app_widget_configure);

        // Инициализация контролов
        InitControls();

        // Извлечение интента
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Извлечение идентификатора виджета из интента
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Если это активити было запущено с интентом без идентификатора виджете - завершить его с ошибкой
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    private void InitControls() {
        // Установка обработчика клика по кнопке "Добавить виджет"
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Создание обьекта репозитория для извлечения списка всех созданых карт
        CardsRepository repository = new CardsRepository(this);
        // Создание адаптера для отображения списка карт
        adapter = new CardsListAdapter(this, repository.getAll());

        lvCardsList = (ListView)findViewById(R.id.lvCardsList);
        // Установка адаптера для списка
        lvCardsList.setAdapter(adapter);
    }

    // Обработчик клика по кнопке "Добавить виджет"
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Извлечение контекста активити
            final Context context = WidgetConfigureActivity.this;

            // Получение списка карт, отмеченых для отображения на виджете
            ArrayList<Card> selectedCards = adapter.getCheckedCardsList();
            ArrayList<Integer> selectedCardsIds = new ArrayList<Integer>();
            // Извлечение идентификаторов из даного списка
            for (Card card : selectedCards){
                selectedCardsIds.add(card.id);
            }
            // Сохранение настроек
            savePreferences(getApplicationContext(), mAppWidgetId, selectedCardsIds);

            // Экран настроек должен обновить созданый виджет
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            WidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Убедимся, что мы передаем обратно оригинальний идентификатор виджета
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            // Завершение настроек, закрытие активити
            finish();
        }
    };

    // Метод сохранения настроек виджета
    static void savePreferences(Context context, int appWidgetId, ArrayList<Integer> cardIds) {
        // Получение редактора настроек
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        // Запись количества карт, необходимых для отображения
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, cardIds.size());
        for (int i = 0; i < cardIds.size(); i++){
            // Запись идентификатора каждой карты, необходимой для отображения
            prefs.putInt(PREF_PREFIX_KEY + appWidgetId + "_" + i, cardIds.get(i));
        }
        // Сохранение настроек
        prefs.commit();
    }

    // Метод получения настроек виджета
    static ArrayList<Integer> getPreferences(Context context, int appWidgetId) {
        // Получение редактора настроек
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        // Получение количества карт, необходимых для отображения
        int cardsCount = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        ArrayList<Integer> cardIds = new ArrayList<Integer>();
        for (int i = 0; i < cardsCount; i++){
            // Получение идентификатора каждой карты, необходимой для отображения
            cardIds.add(prefs.getInt(PREF_PREFIX_KEY + appWidgetId + "_" + i, 0));
        }
        return cardIds;
    }

    // Метод удаления настроек виджета
    static void deletePreferences(Context context, int appWidgetId) {
        // Получение редактора настроек
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        // Удаление записи текущего, удаляемого, виджета
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        // Сохранение настроек
        prefs.commit();
    }
}




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

    private static final String PREFS_NAME = "com.smsbooker.pack.widgets.AppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    ListView lvCardsList;
    CardsListAdapter adapter;

    public WidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.app_widget_configure);

        InitControls();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    private void InitControls() {
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        CardsRepository repository = new CardsRepository(this);
        adapter = new CardsListAdapter(this, repository.getAll());

        lvCardsList = (ListView)findViewById(R.id.lvCardsList);
        lvCardsList.setAdapter(adapter);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetConfigureActivity.this;

            ArrayList<Card> selectedCards = adapter.getCheckedCardsList();
            ArrayList<Integer> selectedCardsIds = new ArrayList<Integer>();
            for (Card card : selectedCards){
                selectedCardsIds.add(card.id);
            }
            savePreferences(getApplicationContext(), mAppWidgetId, selectedCardsIds);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            WidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    // Write the prefix to the SharedPreferences object for this widget
    static void savePreferences(Context context, int appWidgetId, ArrayList<Integer> cardIds) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, cardIds.size());
        for (int i = 0; i < cardIds.size(); i++){
            prefs.putInt(PREF_PREFIX_KEY + appWidgetId + "_" + i, cardIds.get(i));
        }
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static ArrayList<Integer> getPreferences(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int cardsCount = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        ArrayList<Integer> cardIds = new ArrayList<Integer>();
        for (int i = 0; i < cardsCount; i++){
            cardIds.add(prefs.getInt(PREF_PREFIX_KEY + appWidgetId + "_" + i, 0));
        }
        return cardIds;
    }

    static void deletePreferences(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}




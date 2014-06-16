package com.smsbooker.pack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.smsbooker.pack.R;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy on 04.06.2014.
 */
public class TransactionsListActivity extends Activity {

    ExpandableListView elvTransactions;
    TextView tvCardName;

    SimpleExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);

        InitControls();
        InitActivityData();
    }

    private void InitControls() {
        tvCardName = (TextView)findViewById(R.id.tvCardName);

        elvTransactions = (ExpandableListView)findViewById(R.id.elvTransactions);
    }

    private void InitActivityData() {
        Intent intent = getIntent();

        Card card = intent.getParcelableExtra(Card.class.getCanonicalName());
        if (card == null){
            finish();
            return;
        }

        tvCardName.setText(card.name);

        Collections.reverse(card.transactions);

        ArrayList<Map<String, String>> groupsData = new ArrayList<Map<String, String>>();
        ArrayList<ArrayList<Map<String, String>>> childsData = new ArrayList<ArrayList<Map<String, String>>>();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (Transaction transaction : card.transactions){
            Map<String, String> groupItem = new HashMap<String, String>();

            String strValue = transaction.type == Transaction.Type.increment ? "+" : "-";
            strValue += String.format("%.2f", transaction.value);

            groupItem.put("value", strValue);
            groupItem.put("balance", String.format("%.2f", transaction.balance));
            groupItem.put("dateTime", dateFormatter.format(new Date(transaction.createdOn)));

            groupsData.add(groupItem);

            Map<String, String> childItem = new HashMap<String, String>(){};
            childItem.put("messageBody", transaction.message);

            ArrayList<Map<String, String>> childItems = new ArrayList<Map<String, String>>();
            childItems.add(childItem);

            childsData.add(childItems);
        }

        String[] groupFrom = new String[]{ "value", "balance", "dateTime" };
        int[] groupTo = new int[]{ R.id.tvValue, R.id.tvBalance, R.id.tvDateTime };

        String[] childFrom = new String[]{ "messageBody" };
        int[] childTo = new int[]{ R.id.tvMessageBody };

        adapter = new SimpleExpandableListAdapter(
                this,
                groupsData,
                R.layout.transaction_item,
                groupFrom,
                groupTo,
                childsData,
                R.layout.transaction_message_body,
                childFrom,
                childTo
        );

        elvTransactions.setAdapter(adapter);
    }
}

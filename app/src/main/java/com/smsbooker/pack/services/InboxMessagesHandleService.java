package com.smsbooker.pack.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.smsbooker.pack.R;
import com.smsbooker.pack.TransactionsManager;
import com.smsbooker.pack.activities.CardsListActivity;
import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.models.Transaction;
import com.smsbooker.pack.repositories.CardsRepository;
import com.smsbooker.pack.repositories.TransactionsRepository;

public class InboxMessagesHandleService extends Service {

    public InboxMessagesHandleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String smsAddress = intent.getStringExtra("sms_address");
        String smsBody = intent.getStringExtra("sms_body");

        Transaction transaction = TransactionsManager.getTransaction(this, smsAddress, smsBody);
        if (transaction == null){
            return START_NOT_STICKY;
        }

        TransactionsRepository transactionsRepository = new TransactionsRepository(this);
        transactionsRepository.add(transaction);

        CardsRepository cardsRepository = new CardsRepository(this);
        Card card = cardsRepository.getCardById(transaction.cardId);

        if (card == null){
            return START_STICKY;
        }

        int resourceId;
        if (transaction.type == Transaction.Type.increment){
            resourceId = R.string.notification_text_increment;
        } else {
            resourceId = R.string.notification_text_decrement;
        }

        String notificationTitle = String.format(getResources().getString(R.string.notification_title), card.name);
        String notificationText = String.format(getResources().getString(resourceId), transaction.value, transaction.balance);

        showNotification(notificationTitle, notificationText);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(String title, String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, CardsListActivity.class), 0);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notificationManager.notify(R.drawable.ic_launcher, notification);
    }
}

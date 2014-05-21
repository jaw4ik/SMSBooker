package com.smsbooker.pack.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.smsbooker.pack.R;
import com.smsbooker.pack.activities.CardsListActivity;

public class InboxMessagesHandleService extends Service {

    final int NOTIFICATION_ID = 1;

    public InboxMessagesHandleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*Notification notification = new Notification(R.drawable.ic_launcher, "SMS Booker. Сервис запущен!", System.currentTimeMillis());

        Intent intent = new Intent(this, CardsListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notification.setLatestEventInfo(this, "SMS Booker", "SMS Booker. Сервис запущен!", pendingIntent);
        startForeground(NOTIFICATION_ID, notification);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String smsAddress = intent.getStringExtra("sms_address");
        String smsBody = intent.getStringExtra("sms_body");

        showNotification(smsAddress, smsBody);

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(String address, String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, CardsListActivity.class), 0);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(address)
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.ic_launcher, notification);
    }
}

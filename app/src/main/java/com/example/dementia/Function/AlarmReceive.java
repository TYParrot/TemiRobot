package com.example.dementia.Function;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.os.Build;

public class AlarmReceive extends BroadcastReceiver {

    //오레오(안드로이드 8.0) 이상은 반드시 채널을 설정해줘야 Notification이 작동.
    private static final String CHANNEL_ID = "alarm_channel_id";
    private static final String CHAANEL_NAME = "Channel1";

    //수신되는 Intent
    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람이 수신되었을 때 실행될 동작을 정의
        Toast.makeText(context, "알람이 울렸습니다.", Toast.LENGTH_SHORT).show();

        //디버깅용
        System.out.print("알람이 울렸음.");

        createNotificationChannel(context);

        // 알림 빌드
        Notification notification = new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle("Alarm!")
                .setContentText("This is your alarm notification.")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setAutoCancel(true)
                .build();

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private void createNotificationChannel(Context context) {
        // 채널 설정 (Android 8.0 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

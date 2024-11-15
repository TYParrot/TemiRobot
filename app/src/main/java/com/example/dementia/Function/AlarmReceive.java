package com.example.dementia.Function;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.widget.Toast;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.dementia.R;
import com.example.dementia.UI.AlarmNotificationUI;

//정해진 시간에 알림 호출
public class AlarmReceive extends BroadcastReceiver {

    //오레오(안드로이드 8.0) 이상은 반드시 채널을 설정해줘야 Notification이 작동.
    private static final String CHANNEL_ID = "alarm_channel_id";

    //수신되는 Intent
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "알람이 울렸습니다.", Toast.LENGTH_LONG).show();

        // 알람을 save하는 과정에서 생긴 고유의 id를 받아옴.
        int alarmID = intent.getIntExtra("alarm_id", -1);

        //전달받은 고유의 id 확인용
        System.out.println("AlarmReceive 받은 고유 아이디 : " + alarmID);

        createNotificationChannel(context);

        // AlarmNotificationUI에 고유 ID 전달, alarm_id라는 이름으로.
        Intent alarmNotiUI = new Intent(context, AlarmNotificationUI.class);
        alarmNotiUI.putExtra("alarm_id", alarmID); // 고유 ID 전달
        alarmNotiUI.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 알림을 대신해 바로 페이지 전환
        context.startActivity(alarmNotiUI);

        // 알림 빌드
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("적절한 복용 시간!")
                .setContentText("지금 약을 섭취하면 가장 효과적이에요!")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentIntent(PendingIntent.getActivity(context, alarmID, alarmNotiUI, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                .setAutoCancel(true) // 알림을 탭하면 자동으로 사라짐
                .build();

        // 고유 ID로 알림 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(alarmID, notification); // 고유 ID로 알림 표시
        }
    }

    private void createNotificationChannel(Context context) {
        // 채널 설정 (Android 8.0 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 알림 사운드 URI 생성
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm1);

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            // 알림 채널에 사운드와 AudioAttributes 설정 추가
            channel.setSound(soundUri, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

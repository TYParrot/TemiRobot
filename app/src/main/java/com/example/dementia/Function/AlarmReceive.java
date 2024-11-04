package com.example.dementia.Function;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.dementia.UI.AlarmNotificationUI;

//정해진 시간에 알림 호출
public class AlarmReceive extends BroadcastReceiver {

    //오레오(안드로이드 8.0) 이상은 반드시 채널을 설정해줘야 Notification이 작동.
    private static final String CHANNEL_ID = "alarm_channel_id";
    private static final String CHAANEL_NAME = "Channel1";

    /*NotificationCompat.Builder.build()의 결과를 전달한다.
            setContentTitle() : 알림 제목을 설정한다.
            setContentText() : 알림의 본문을 설정한다.
            setSmallIcon() : 알림의 아이콘을 설정한다.
            setAutoCancel() : true로 설정할 시 알림을 탭하면 삭제할 수 있다.
    setContentIntent() : 알림을 눌렀을 때 실행할 작업 인텐트를 설정한다.*/

    //수신되는 Intent
    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람이 수신되었을 때 실행될 동작을 정의
        Toast.makeText(context, "알람이 울렸습니다.", Toast.LENGTH_SHORT).show();

        createNotificationChannel(context);

        Intent alarmNotiUI = new Intent(context, AlarmNotificationUI.class);
        alarmNotiUI.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNotiUI = PendingIntent.getActivity(context, 0, alarmNotiUI, PendingIntent.FLAG_CANCEL_CURRENT);

        // 알림 빌드
        Notification notification = new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle("적절한 복용 시간!")
                .setContentText("지금 약을 섭취하면 가장 효과적이에요!")
                //설정한 알약 이미지를 넣어주는 것으로 바꿔야 함.
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                //알림을 탭하면 삭제하는 기능이 아닌, 어플로 돌아가 '복용 완료'버튼을 눌러야 끝나도록.
                //.setAutoCancel(true)
                .setContentIntent(pendingNotiUI)
                .build();

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(1, notification);
        }

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

package com.example.dementia.Function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.dementia.UI.AlarmNotificationUI;

//정해진 시간에 알람 호출
public class AlarmReceive extends BroadcastReceiver {

    //수신되는 Intent
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "알람이 울렸습니다.", Toast.LENGTH_LONG).show();

        // 알람을 save하는 과정에서 생긴 고유의 id를 받아옴.
        int alarmID = intent.getIntExtra("alarm_id", -1);

        //전달받은 고유의 id 확인용
        System.out.println("AlarmReceive 받은 고유 아이디 : " + alarmID);

        // AlarmNotificationUI에 고유 ID 전달, alarm_id라는 이름으로.
        Intent alarmNotiUI = new Intent(context, AlarmNotificationUI.class);
        alarmNotiUI.putExtra("alarm_id", alarmID); // 고유 ID 전달
        alarmNotiUI.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 알림을 대신해 바로 페이지 전환
        context.startActivity(alarmNotiUI);
    }
}

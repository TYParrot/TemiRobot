package com.example.dementia.Manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.dementia.Function.AlarmList;
import com.example.dementia.Function.AlarmDataSave;
import com.example.dementia.Function.AlarmReceive;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

//실질적으로 알람을 예약하는 클래스.
//고유의 ID로 생성된 알림들이기 때문에, 재부팅할 때도 data를 하나씩 불러와서 불러오도록 해야함. 이는 다른 클래스에서 진행할 예정.
public class AlarmManager {

    private static AlarmList alarmList;
    private android.app.AlarmManager systemAlarmManager; // 시스템 AlarmManager
    private Context currentContext;

    //고유한 알림id 생성을 위한 AtomicInteger
    private static final AtomicInteger uniqueID = new AtomicInteger(0);

    // init 메서드에서 시스템 AlarmManager 초기화
    //requestCode 확인 필요
    public void init(Context context) {
        currentContext = context;
        systemAlarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    //release필요. 지역 변수이므로, AlarmSave에서 작성.
    public AlarmDataSave getAlarmDataSave(){
        return new AlarmDataSave();
    }

    //알람 예약
    public int makeAlarm(int hour, int minute, boolean[] dayClicked) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        boolean alarmSet = false;

        // 선택된 요일만 등록
        for (int i = 0; i < dayClicked.length; i++) {
            if (dayClicked[i]) {
                int day = i + 1; // Calendar.SUNDAY == 1
                calendar.set(Calendar.DAY_OF_WEEK, day);

                if (calendar.getTimeInMillis() >= System.currentTimeMillis()) {
                    // 현재 시간보다 미래의 경우, 해당 시간에 알람 설정
                    alarmSet = true;
                    break;
                }
            }
        }

        // 모든 요일을 검사했지만, 현재 시간보다 늦은 시간이 없을 경우 - 1분 뒤로 설정
        if (!alarmSet) {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1);
        }

        //고유 알림 id 생성함. 호출할 때마다 다른 id를 생성.
        int alarmID = uniqueID.incrementAndGet();

        Intent intent = new Intent(currentContext, AlarmReceive.class);
        intent.setAction("com.example.dementia.ACTION_ALARM_RECEIVE"); // Action 추가
        intent.putExtra("alarm_id",alarmID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(currentContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (systemAlarmManager != null) {
            systemAlarmManager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        System.out.println("알람 매니저"+alarmID);

        //방금 예약한 알람에 대한 고유 아이디를 반환.
        return alarmID;
    }

}

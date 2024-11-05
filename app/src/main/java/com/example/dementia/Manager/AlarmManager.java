package com.example.dementia.Manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.dementia.Function.AlarmListDataSet;
import com.example.dementia.Function.AlarmDataReserve;
import com.example.dementia.Function.AlarmReceive;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

//실질적으로 알람을 예약하는 클래스.
//알람 데이터 관리 클래스도 여기서 하나의 객체로 관리...
//고유의 ID로 생성된 알림들이기 때문에, 재부팅할 때도 data를 하나씩 불러와서 불러오도록 해야함. 이는 다른 클래스에서 진행할 예정.
public class AlarmManager {

    private static AlarmDataManager alarmDataManager;
    private android.app.AlarmManager systemAlarmManager; // 시스템 AlarmManager
    private Context currentContext;

    //고유한 알림id 생성을 위한 AtomicInteger
    private static final AtomicInteger uniqueID = new AtomicInteger(0);

    // init 메서드에서 시스템 AlarmManager 초기화
    //requestCode 확인 필요
    public void init(Context context) {
        currentContext = context;
        systemAlarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //1개만.
        alarmDataManager = new AlarmDataManager();
    }

    //release필요. 지역 변수이므로, AlarmSave에서 작성.
    public AlarmDataReserve getAlarmDataReserve(){
        return new AlarmDataReserve();
    }

    public AlarmDataManager getAlarmDataManager(){
        return alarmDataManager;
    }

    // 알람 예약
    public int makeAlarm(int hour, int minute, boolean[] dayClicked) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 선택된 요일만 등록
        boolean alarmSet = false;
        for (int i = 0; i < dayClicked.length; i++) {
            if (dayClicked[i]) {
                int day;

                // 요일 설정: 일요일이 1, 토요일이 7
                day = (i != 6) ? i + 2 : 1; // 일요일은 1로 설정

                calendar.set(Calendar.DAY_OF_WEEK, day);

                if (calendar.getTimeInMillis() >= System.currentTimeMillis()) {
                    alarmSet = true;
                    break;
                }
            }
        }

        // 모든 요일을 지나친 경우, 다음 주의 첫 번째 요일로 설정
        if (!alarmSet) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // 고유 알림 ID 생성
        int alarmID = uniqueID.incrementAndGet();

        Intent intent = new Intent(currentContext, AlarmReceive.class);
        intent.setAction("com.example.dementia.ACTION_ALARM_RECEIVE");
        intent.putExtra("alarm_id", alarmID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(currentContext, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (systemAlarmManager != null) {
            // 매주 반복되는 요일별 알람 설정
            systemAlarmManager.setRepeating(
                    android.app.AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    android.app.AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
            );
        }

        System.out.println("알람 매니저 " + alarmID);

        // 예약한 알람의 고유 ID 반환
        return alarmID;
    }

}

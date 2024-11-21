package com.example.dementia.Manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.dementia.Function.AlarmListDataSet;
import com.example.dementia.Function.AlarmDataReserve;
import com.example.dementia.Function.AlarmReceive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

//실질적으로 알람을 예약하는 클래스.
//알람 데이터 관리 클래스도 여기서 하나의 객체로 관리...
//고유의 ID로 생성된 알림들이기 때문에, 재부팅할 때도 data를 하나씩 불러와서 불러오도록 해야함. 이는 다른 클래스에서 진행할 예정.
import android.content.SharedPreferences;

public class AlarmManager {

    private static final String PREF_NAME = "AlarmPrefs";
    private static final String KEY_LAST_ALARM_ID = "LastAlarmID";

    private Context currentContext;
    private static AlarmDataManager alarmDataManager;
    private android.app.AlarmManager systemAlarmManager;

    public void init(Context context) {
        currentContext = context;
        systemAlarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmDataManager = new AlarmDataManager(context);
    }

    // 알람 ID를 저장
    private void saveLastAlarmID(int lastID) {
        SharedPreferences preferences = currentContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_LAST_ALARM_ID, lastID);
        editor.apply();
    }

    // 저장된 마지막 알람 ID를 로드
    private int loadLastAlarmID() {
        SharedPreferences preferences = currentContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_LAST_ALARM_ID, 0); // 기본값은 0
    }

    // 알람 예약
    public int makeAlarm(int hour, int minute, boolean[] dayClicked) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        boolean alarmSet = false;
        for (int i = 0; i < dayClicked.length; i++) {
            if (dayClicked[i]) {
                int day = (i != 6) ? i + 2 : 1; // 일요일은 1로 설정
                calendar.set(Calendar.DAY_OF_WEEK, day);

                if (calendar.getTimeInMillis() >= System.currentTimeMillis()) {
                    alarmSet = true;
                    break;
                }
            }
        }

        if (!alarmSet) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // 고유 알림 ID 생성
        int lastID = loadLastAlarmID();
        int alarmID = lastID + 1;
        saveLastAlarmID(alarmID); // 새 알람 ID 저장

        Intent intent = new Intent(currentContext, AlarmReceive.class);
        intent.setAction("com.example.dementia.ACTION_ALARM_RECEIVE");
        intent.putExtra("alarm_id", alarmID);

        //핸드폰 테스트
        PendingIntent pendingIntent = PendingIntent.getBroadcast(currentContext, alarmID, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        if (systemAlarmManager != null) {
            systemAlarmManager.setRepeating(
                    android.app.AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    android.app.AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
            );
        }

        System.out.println("알람 매니저 " + alarmID);
        return alarmID;
    }


    //release필요. 지역 변수이므로, AlarmSave에서 작성.
    public AlarmDataReserve getAlarmDataReserve(){
        return new AlarmDataReserve();
    }

    public AlarmDataManager getAlarmDataManager(){
        return alarmDataManager;
    }

    //앱 재 가동시 알림 예약도 다시 해야함
    public void restoreAlarms() {
        ArrayList<AlarmListDataSet> savedAlarms = AlarmListDataSet.loadFromJSONFile(currentContext); // 저장된 알람 데이터 로드

        for (AlarmListDataSet alarm : savedAlarms) {
            int alarmID = alarm.getAlarmID();
            boolean[] selectedDays = alarm.getSelectedDays();
            String[] timeParts = alarm.getSelectedTime().split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // 알람을 다시 예약
            for (int i = 0; i < selectedDays.length; i++) {
                if (selectedDays[i]) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    int day = (i != 6) ? i + 2 : 1; // 일요일은 1로 설정
                    calendar.set(Calendar.DAY_OF_WEEK, day);

                    if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        calendar.add(Calendar.WEEK_OF_YEAR, 1); // 다음 주로 이동
                    }

                    Intent intent = new Intent(currentContext, AlarmReceive.class);
                    intent.setAction("com.example.dementia.ACTION_ALARM_RECEIVE");
                    intent.putExtra("alarm_id", alarmID);

                    //핸드폰용 PendingIntent
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(currentContext, alarmID, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    systemAlarmManager.setRepeating(
                            android.app.AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            android.app.AlarmManager.INTERVAL_DAY * 7,
                            pendingIntent
                    );
                }
            }
        }
    }

}

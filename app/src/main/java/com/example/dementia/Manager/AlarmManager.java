package com.example.dementia.Manager;

import android.content.Context;

import com.example.dementia.Function.AlarmList;
import com.example.dementia.Function.AlarmDataSave;

import java.util.Calendar;

//알람 관련 관리 클래스
//알람 목록은 상태를 유지해야하므로, 한 번만 생성되야함.
//다른 것들은 그때 그때 생성 및 반환
public class AlarmManager {

    private static AlarmList alarmList;
    private android.app.AlarmManager systemAlarmManager; // 시스템 AlarmManager

    // init 메서드에서 시스템 AlarmManager 초기화
    public void init(Context context) {
        systemAlarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    //release필요. 지역 변수이므로, AlarmSave에서 작성.
    public AlarmDataSave getAlarmDataSave(){
        return new AlarmDataSave();
    }

    //알람 예약
    public void makeAlarm(int hour, int minute, boolean[] dayClicked){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        //선택된 요일만 등록
        for(int i = 0; i<dayClicked.length; i++){
            if(dayClicked[i]){
                //1: 월, 7:일
                int day = i+1;
                calendar.set(Calendar.DAY_OF_WEEK, 1);

                if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                    calendar.add(Calendar.WEEK_OF_YEAR,1);
                }
            }
        }

    }
}

package com.example.dementia.Manager;

import com.example.dementia.Function.AlarmList;
import com.example.dementia.Function.AlarmSet;

//알람 관련 관리 클래스
//알람 목록은 상태를 유지해야하므로, 한 번만 생성되야함.
//다른 것들은 그때 그때 생성 및 반환
public class AlarmManager {

    private static AlarmList alarmList;


    public void init(){


    }

    //release필요. 지역 변수이므로, AlarmSet에서 작성.
    public AlarmSet getAlarmSet(){
        return new AlarmSet();
    }
}

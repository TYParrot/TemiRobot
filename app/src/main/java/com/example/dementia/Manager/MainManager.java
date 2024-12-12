package com.example.dementia.Manager;

import android.content.Context;

import com.example.dementia.MainActivity;
import com.robotemi.sdk.Robot;

//MainActivity에서 생성, 다른 매니저들을 관리함.
public class MainManager {

    private static MainManager manager;

    private AlarmManager alarm;

    //인스턴스 생성시 실행됨.
    private MainManager(){
        alarm = new AlarmManager();
    }

    public static MainManager getMain(){
        if(manager == null){
            manager = new MainManager();
        }
        return manager;
    }

    //초기화
    public void initAlarmManager(Context context){
        alarm.init(context);
    }

    //알람 매니저 반환, 관련 메소드 접근
    public AlarmManager getAlarm(){
        return alarm;
    }

}

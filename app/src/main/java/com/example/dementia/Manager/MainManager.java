package com.example.dementia.Manager;

//MainActivity에서 생성, 다른 매니저들을 관리함.
public class MainManager {

    private static MainManager manager;

    private AlarmManager alarm;

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
    public void init(){
        alarm.init();
    }

    //알람 매니저 반환, 관련 메소드 접근
    public AlarmManager getAlarm(){
        return alarm;
    }
}

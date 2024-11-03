package com.example.dementia.Function;

import com.example.dementia.Manager.AlarmManager;
import com.example.dementia.Manager.MainManager;

//실질적으로 알람을 저장하는 기능 클래스
public class AlarmSet {

    private static AlarmManager Alarm;



    private void initManager(){
        Alarm = MainManager.getMain().getAlarm();
    }

    //참조를 해제하겨 가비지 컬렉션이 되도록 함.
    public void release(){
        Alarm = null;
    }


    //선택 정보 데이터 저장 필요.
    public void save(){

    }

}

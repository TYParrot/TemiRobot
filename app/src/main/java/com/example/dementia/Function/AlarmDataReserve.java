package com.example.dementia.Function;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.dementia.Manager.AlarmManager;
import com.example.dementia.Manager.MainManager;

import java.util.Calendar;

//실질적으로 알람 예약을 위해 전달하는 기능 클래스
public class AlarmDataReserve {

    private static AlarmManager Alarm;
    private int personalID;

    public void initManager(){
        this.Alarm = MainManager.getMain().getAlarm();
    }

    //참조를 해제하고 가비지 컬렉션이 되도록 함.
    //취소할 때도 release가 필요하므로 public으로 설정.
    public void release(){
        Alarm = null;
    }


    //알람 예약 요청
    public int reserveData(int hour, int minute, boolean[] dayClicked){
        //고유의 알림id를 반환받아와야 함.
        personalID = Alarm.makeAlarm(hour, minute, dayClicked);

        release();

        return personalID;
    }

}

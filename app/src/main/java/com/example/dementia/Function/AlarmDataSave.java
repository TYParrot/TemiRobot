package com.example.dementia.Function;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.dementia.Manager.AlarmManager;
import com.example.dementia.Manager.MainManager;

import java.util.Calendar;

//실질적으로 알람을 저장하고 예약을 위해 전달하는 기능 클래스
//저장된 데이터들을 식별하기 위해서 아이디를 읽어올 필요가 있음.
//AlarmData가 필요할듯?
public class AlarmDataSave{

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


    //알람 정보 저장 및 예약 요청
    public void saveData(int hour, int minute, boolean[] dayClicked){
        //고유의 알림id를 반환받아와야 함.
        personalID = Alarm.makeAlarm(hour, minute, dayClicked);

        //알람 정보를 저장하는 로직 구현 필요.
        //배열의 첫번째에는 personalID로 확인.
        //한줄로 저장하고 ,로 파싱?
        System.out.println("알람데이터 저장"+personalID);

        release();
    }

}

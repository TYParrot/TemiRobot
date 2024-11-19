package com.example.dementia.Manager;

import android.content.Context;
import android.net.Uri;

import com.example.dementia.Function.AlarmListDataSet;

import java.util.ArrayList;

public class AlarmDataManager {
    private ArrayList<AlarmListDataSet> alarmList;

    public AlarmDataManager(Context context) {
        alarmList = new ArrayList<>();

        //앱만 재구동했을 때도 데이터를 불러와야 함.
        loadFromFile(context);
    }

    // 알람 추가 메소드
    public void saveData(Context context, int alarmID, String pillImgUri, boolean[] selectedDays, int hour, int minute) {
        AlarmListDataSet newAlarm = new AlarmListDataSet(alarmID, pillImgUri, selectedDays, hour, minute);
        alarmList.add(newAlarm);

        //파일로 저장
        saveToFile(context);
    }

    private void saveToFile(Context context) {
        AlarmListDataSet.saveToJSONFile(context, alarmList);
    }

    public void loadFromFile(Context context){
        alarmList = AlarmListDataSet.loadFromJSONFile(context);
    }

    // 알람 조회 메소드 (ID로 찾기)
    public AlarmListDataSet getAlarmById(int alarmID) {
        for (AlarmListDataSet alarm : alarmList) {
            if (alarm.getAlarmID() == alarmID) {
                return alarm; // 해당 ID의 알람 데이터 반환
            }
        }
        return null; // 없으면 null 반환
    }

    // 모든 알람 조회 메소드
    public ArrayList<AlarmListDataSet> getAllAlarms() {
        return alarmList;
    }

}


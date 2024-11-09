package com.example.dementia.Manager;

import android.net.Uri;

import com.example.dementia.Function.AlarmListDataSet;

import java.util.ArrayList;

public class AlarmDataManager {
    private ArrayList<AlarmListDataSet> alarmList;

    public AlarmDataManager() {
        alarmList = new ArrayList<>();
    }

    // 알람 추가 메소드
    public void saveData(int alarmID, Uri pillImgUri, boolean[] selectedDays, int hour, int minute) {
        AlarmListDataSet newAlarm = new AlarmListDataSet(alarmID, pillImgUri, selectedDays, hour, minute);
        alarmList.add(newAlarm);
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


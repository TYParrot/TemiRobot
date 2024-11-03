package com.example.dementia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.MainActivity;
import com.example.dementia.R;

//생성된 알람 목록 확인
//알람 기능 확장에 따라서 받아오는 값이 있어야 함.
public class AlarmListUI extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmlist);

        convertPage();
    }

    //알람 목록 페이지 최상단일 시, 목록을 갱신해줘야 함.
    @Override
    protected void onResume(){
        super.onResume();
        refreshAlarmList();
    }

    //알람 목록 갱신 및 데이터 불러오기.
    private void refreshAlarmList(){

    }

    //페이지 전환
    private void convertPage(){
        Button alarmAdd = findViewById(R.id.alarmAddBtn);
        alarmAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent alarmSet = new Intent(AlarmListUI.this, AlarmSetUI.class);
                startActivity(alarmSet);
            }
        });

        Button listToMain = findViewById(R.id.alarmListBackBtn);
        listToMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent alarmListBack = new Intent(AlarmListUI.this, MainActivity.class);
                finish();
                AlarmListUI.super.onBackPressed();
            }
        });
    }
}

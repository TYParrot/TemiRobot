package com.example.dementia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dementia.Function.AlarmListAdapter;
import com.example.dementia.Function.AlarmListDataSet;
import com.example.dementia.MainActivity;
import com.example.dementia.Manager.MainManager;
import com.example.dementia.R;
import com.example.dementia.Manager.AlarmDataManager;

import java.util.ArrayList;


//생성된 알람 목록 확인
//알람 기능 확장에 따라서 받아오는 값이 있어야 함.
public class AlarmListUI extends AppCompatActivity {

    private AlarmDataManager alarmDataManager;
    private ArrayList<AlarmListDataSet> alarmListData;
    //Adapter 설정
    private AlarmListAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        alarmDataManager = MainManager.getMain().getAlarm().getAlarmDataManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmlist);

        //변수 초기화
        recyclerView = findViewById(R.id.alarmRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //알람 목록 호출
        refreshAlarmList();

        //페이지 전환 이벤트(사실상 버튼 초기화)
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
        alarmListData = alarmDataManager.getAllAlarms();
        adapter = new AlarmListAdapter(alarmListData);

        if(alarmListData == null){
            alarmListData = new ArrayList<>();
        }
        recyclerView.setAdapter(adapter);
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
                finish();
                AlarmListUI.super.onBackPressed();
            }
        });
    }
}

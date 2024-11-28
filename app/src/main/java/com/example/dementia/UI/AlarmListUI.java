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
import com.example.dementia.Manager.MainManager;
import com.example.dementia.R;
import com.example.dementia.Manager.AlarmDataManager;

import java.util.ArrayList;

public class AlarmListUI extends AppCompatActivity {

    private AlarmDataManager alarmDataManager;
    private ArrayList<AlarmListDataSet> alarmListData;
    private AlarmListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmlist);

        initialize();
        refreshAlarmList();
        setupPageTransitions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAlarmList();
    }

    /**
     * 초기화: 데이터 매니저, RecyclerView 설정
     */
    private void initialize() {
        alarmDataManager = MainManager.getMain().getAlarm().getAlarmDataManager();
        recyclerView = findViewById(R.id.alarmRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 알람 목록 갱신 및 데이터 불러오기
     */
    private void refreshAlarmList() {
        alarmDataManager.loadFromFile(this);
        alarmListData = alarmDataManager.getAllAlarms();

        if (alarmListData == null) {
            alarmListData = new ArrayList<>();
        }

        if (adapter == null) {
            adapter = new AlarmListAdapter(alarmListData);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(alarmListData);
        }
    }

    /**
     * 페이지 전환 버튼 설정
     */
    private void setupPageTransitions() {
        Button alarmAddButton = findViewById(R.id.alarmAddBtn);
        alarmAddButton.setOnClickListener(view -> {
            Intent alarmSetIntent = new Intent(AlarmListUI.this, AlarmSetUI.class);
            startActivity(alarmSetIntent);
        });

        Button backToMainButton = findViewById(R.id.alarmListBackBtn);
        backToMainButton.setOnClickListener(view -> {
            finish();
            super.onBackPressed();
        });
    }
}

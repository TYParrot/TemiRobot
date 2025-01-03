package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dementia.Function.AlarmDataReserve;
import com.example.dementia.Manager.AlarmDataManager;
import com.example.dementia.Manager.MainManager;
import com.example.dementia.R;

public class AlarmSetUI extends AppCompatActivity {

    private AlarmDataReserve alarmReserve;
    private AlarmDataManager alarmDataManager;

    private int alarmID;
    private String imgUri; // 선택된 알약 이미지 이름
    private TimePicker timePicker;
    private int hour, minute;

    private ImageView pillImg;
    private Button[] dayBtns;
    private boolean[] dayBtnsClicked; // 요일 버튼 클릭 상태 저장

    private int[] currentImageIndex = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmset);

        // 초기화
        initVariables();

        // 이벤트 설정
        setupEventListeners();

        // 페이지 전환 버튼 설정
        setupPageTransitions();
    }

    // 변수 초기화
    private void initVariables() {
        pillImg = findViewById(R.id.pillImg);
        timePicker = findViewById(R.id.timePicker);

        dayBtns = new Button[]{
                findViewById(R.id.monBtn),
                findViewById(R.id.tueBtn),
                findViewById(R.id.wedBtn),
                findViewById(R.id.thuBtn),
                findViewById(R.id.friBtn),
                findViewById(R.id.satBtn),
                findViewById(R.id.sunBtn)
        };

        dayBtnsClicked = new boolean[7];
        imgUri = "pill"; // 기본 알약 이미지 설정

        // AlarmDataReserve 및 AlarmDataManager 초기화
        alarmReserve = MainManager.getMain().getAlarm().getAlarmDataReserve();
        alarmReserve.initManager();
        alarmDataManager = MainManager.getMain().getAlarm().getAlarmDataManager();
    }

    // 페이지 전환 및 저장/취소 버튼 설정
    private void setupPageTransitions() {
        Button cancel = findViewById(R.id.cancelAlarmBtn);
        cancel.setOnClickListener(view -> {
            // 리소스 해제
            if (alarmReserve != null) {
                alarmReserve.release();
                alarmReserve = null;
            }

            // AlarmListUI로 이동
            Intent setToListBack = new Intent(AlarmSetUI.this, AlarmListUI.class);
            finish();
        });

        Button save = findViewById(R.id.saveAlarmBtn);
        save.setOnClickListener(view -> {
            // 선택된 요일이 있는지 확인
            int dayCount = 0;
            for (boolean clicked : dayBtnsClicked) {
                if (clicked) dayCount++;
            }

            if (dayCount > 0) {
                // 시간 및 요일 저장
                hour = timePicker.getHour();
                minute = timePicker.getMinute();

                // 알람 예약 및 고유 ID 생성
                alarmID = alarmReserve.reserveData(hour, minute, dayBtnsClicked);
                Toast.makeText(this, "알람이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                // 데이터 저장
                saveData();

                // 리소스 해제
                if (alarmReserve != null) alarmReserve.release();

                // AlarmListUI로 이동
                Intent setToListBack = new Intent(AlarmSetUI.this, AlarmListUI.class);
                finish();
            } else {
                Toast.makeText(this, "요일을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 알람 데이터를 저장
    private void saveData() {
        alarmDataManager.saveData(this, alarmID, imgUri, dayBtnsClicked, hour, minute);
    }

    // 이벤트 설정
    private void setupEventListeners() {
        //알약 이미지 선택
        pillImg.setOnClickListener(view -> {
            // 이미지를 순환하기 위해 배열로 관리
            int[] pillImages = {
                    R.drawable.pill,
                    R.drawable.pill1,
                    R.drawable.pill3,
                    R.drawable.pill2,
                    R.drawable.pill4
            };

            // 클릭할 때마다 다음 이미지로 변경
            currentImageIndex[0] = (currentImageIndex[0] + 1) % pillImages.length;
            int currentImageResource = pillImages[currentImageIndex[0]];

            try {
                // 이미지 변경
                pillImg.setImageResource(currentImageResource);

                // 이미지 리소스 이름 추출
                imgUri = getResources().getResourceEntryName(currentImageResource);

            } catch (Resources.NotFoundException e) {
                // 예외가 발생할 경우 처리
                e.printStackTrace();
            }
        });

        // 요일 버튼 클릭 이벤트
        for (int i = 0; i < dayBtns.length; i++) {
            final int index = i;
            dayBtns[i].setOnClickListener(view -> {
                // 상태 반전
                dayBtnsClicked[index] = !dayBtnsClicked[index];

                // 버튼 배경색 변경
                dayBtns[index].setBackgroundColor(getResources().getColor(
                        dayBtnsClicked[index] ? R.color.alarmBtnClickedColor : R.color.white));
            });
        }
    }
}

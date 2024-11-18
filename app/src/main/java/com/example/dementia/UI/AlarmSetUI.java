package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dementia.Function.AlarmDataReserve;
import com.example.dementia.Manager.AlarmDataManager;
import com.example.dementia.Manager.MainManager;
import com.example.dementia.R;

import java.util.ArrayList;

//알람 생성 페이지에서 알람을 설정
//Save를 하면 AlarmListDataSet에 데이터를 저장도 해야함.

public class AlarmSetUI extends AppCompatActivity {

    private AlarmDataReserve alarmReserve;
    private AlarmDataManager alarmDataManager;
    //알림 고유 식별자
    private int alarmID;
    private String imgUri;
    private TimePicker timePicker;
    private int hour;
    private int minute;
    private ImageView pillImg;
    private Button[] dayBtns;
    //day 버튼 클릭 유무 저장 배열
    private boolean[] dayBtnsClicked;
    private static final int REQUEST_CODE_PICK_PILL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarmset);

        //알람 예약 클래스 호출
        alarmReserve = MainManager.getMain().getAlarm().getAlarmDataReserve();
        alarmReserve.initManager();

        //알람 정보 저장 클래스 호출
        alarmDataManager = MainManager.getMain().getAlarm().getAlarmDataManager();

        //변수 초기화 호출
        varInit();

        //버튼 이벤트 세팅
        eventSetting();

        //페이지 전환 호출
        convertPage();
    }

    //변수 초기화
    private void varInit(){

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

        //선택된 이미지 없을 시 초기화 필요
        imgUri = "pill";
    }

    //페이지 전환 전에 필요한 release나 저장 메소드 호출을 함께 관리한다.
    private void convertPage(){
        Button cancel = findViewById(R.id.cancelAlarmBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlarmReserve 참조 해제
                if(alarmReserve != null){
                    alarmReserve.release();
                    alarmReserve = null;
                }
                
                Intent setToListBack = new Intent(AlarmSetUI.this, AlarmListUI.class);
                finish();
                AlarmSetUI.super.onBackPressed();
            }
        });

        Button save = findViewById(R.id.saveAlarmBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int dayCount = 0;
                for(int i = 0; i<dayBtnsClicked.length; i++){
                    if(dayBtnsClicked[i]){
                        dayCount++;
                    }
                }

                if(dayCount != 0){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();

                    alarmID = alarmReserve.reserveData(hour, minute, dayBtnsClicked);
                    Toast.makeText(AlarmSetUI.this, "알람이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                    //데이터 저장
                    saveData();

                    if(alarmReserve != null){
                        alarmReserve.release();
                    }

                    Intent setToListBack = new Intent(AlarmSetUI.this, AlarmListUI.class);
                    finish();
                }else{
                    Toast.makeText(AlarmSetUI.this, "요일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //데이터를 AlarmListDataSet에 저장
    private void saveData(){

        alarmDataManager.saveData(alarmID, imgUri, dayBtnsClicked, hour, minute);

    }

    //이미지 선택 요청 코드
    private static final int PICK_IMAGE = 1;

    //데이터를 세팅하는 버튼들 리스너
    private void eventSetting(){
        //이미지 로드 이벤트
        pillImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pillIntent = new Intent(AlarmSetUI.this, PillSelection.class);
                startActivityForResult(pillIntent, REQUEST_CODE_PICK_PILL);
            }
        });

        //요일 버튼 이벤트
        for(int i = 0; i<dayBtns.length; i++){
            final int index = i;
            dayBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //현재 상태 반전
                    dayBtnsClicked[index] = !dayBtnsClicked[index];

                    //컬러 반전. colors.xml에서 색상 정보 변경
                    if(dayBtnsClicked[index]){
                        dayBtns[index].setBackgroundColor(getResources().getColor(R.color.alarmBtnClickedColor));
                    }else{
                        dayBtns[index].setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            });
        }
    }

    //사용자 선택 이미지
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PILL && resultCode == RESULT_OK) {
            // 사용자가 선택한 알약 이미지 처리
            String selectedPill = data.getStringExtra("selectedPill");
            // 예시로 선택한 알약 이름을 이미지로 변환해서 표시
            // 실제로는 이미지 URI나 경로로 설정할 수 있습니다.
            pillImg.setImageResource(getResources().getIdentifier(selectedPill, "drawable", getPackageName()));
            imgUri = selectedPill;
        }
    }

}
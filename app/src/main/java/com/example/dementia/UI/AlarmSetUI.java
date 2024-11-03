package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dementia.Function.AlarmDataSave;
import com.example.dementia.Manager.MainManager;
import com.example.dementia.R;

//알람 생성 페이지에서 알람을 설정
//데이터는 AlarmSet으로 넘길 예정
//AlarmSet 초기화 필요.
public class AlarmSetUI extends AppCompatActivity {

    private AlarmDataSave alarmSave;
    private TimePicker timePicker;
    private int hour;
    private int minute;
    private ImageView pillImg;
    private Button[] dayBtns;
    //day 버튼 클릭 유무 저장 배열
    private boolean[] dayBtnsClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarmset);

        //알람 기능 클래스 호출
        alarmSave = MainManager.getMain().getAlarm().getAlarmDataSave();
        alarmSave.initManager();

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
    }

    //페이지 전환 전에 필요한 release나 저장 메소드 호출을 함께 관리한다.
    private void convertPage(){
        Button cancel = findViewById(R.id.cancelAlarmBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlarmSet 참조 해제
                if(alarmSave != null){
                    alarmSave.release();
                    alarmSave = null;
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

                    alarmSave.saveData(hour, minute, dayBtnsClicked);
                    Toast.makeText(AlarmSetUI.this, "알람이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                    if(alarmSave != null){
                        alarmSave.release();
                    }

                    Intent setToListBack = new Intent(AlarmSetUI.this, AlarmListUI.class);
                    finish();
                }else{
                    Toast.makeText(AlarmSetUI.this, "요일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //이미지 선택 요청 코드
    private static final int PICK_IMAGE = 1;

    //데이터를 세팅하는 버튼들 리스너
    private void eventSetting(){
        //이미지 로드 이벤트
        pillImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
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

    //이미지 선택 및 로드
    //아래 부분에서 뜨는 경고 오류 메세지 무시할 것.
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                ;
        startActivityForResult(gallery, PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri imgUri = data.getData();
            pillImg.setImageURI(imgUri);
        }
    }

    
}
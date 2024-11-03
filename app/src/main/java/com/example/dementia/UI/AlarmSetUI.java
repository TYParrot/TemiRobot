package com.example.dementia.UI;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.Spinner;
import com.example.dementia.Function.AlarmSet;
import com.example.dementia.Manager.MainManager;
import com.example.dementia.R;

//알람 생성 페이지에서 알람을 설정
//데이터는 AlarmSet으로 넘길 예정
//AlarmSet 초기화 필요.
public class AlarmSetUI extends AppCompatActivity {

    private AlarmSet alarmSet;

    private ImageView pillImg;
    private Spinner ampm;
    private Spinner hour;
    private Spinner minute;
    private Button[] dayBtns;
    //day 버튼 클릭 유무 저장 배열
    private boolean[] dayBtnsClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmset);

        //알람 기능 클래스 호출
        alarmSet = MainManager.getMain().getAlarm().getAlarmSet();

        //변수 초기화 호출
        varInit();

        //Spinner 목록 불러오기
        initSpinner();

        //버튼 이벤트 세팅
        eventSetting();

        //페이지 전환 호출
        convertPage();
    }

    //변수 초기화
    private void varInit(){
        pillImg = findViewById(R.id.pillImg);
        ampm = findViewById(R.id.ampm);
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);

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

    //Spinner 목록을 string.xml으로부터 불러와서 layout의 spinner에 할당함.
    private void initSpinner(){
        ArrayAdapter<CharSequence> ampmAdapter = ArrayAdapter.createFromResource(this, R.array.ampm_array, android.R.layout.simple_spinner_item);
        ampmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ampm.setAdapter(ampmAdapter);

        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this, R.array.hour_array, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hour.setAdapter(hourAdapter);

        ArrayAdapter<CharSequence> minuteAdapter = ArrayAdapter.createFromResource(this, R.array.minutes_array, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minute.setAdapter(minuteAdapter);
    }

    //페이지 전환 전에 필요한 release나 저장 메소드 호출을 함께 관리한다.
    private void convertPage(){
        Button cancel = findViewById(R.id.cancelAlarmBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlarmSet 참조 해제
                if(alarmSet != null){
                    alarmSet.release();
                    alarmSet = null;
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

                alarmSet.save();

                if(alarmSet != null){
                    alarmSet.release();;
                    alarmSet = null;
                }

                Intent setToListBack = new Intent(AlarmSetUI.this, AlarmListUI.class);
                finish();
                AlarmSetUI.super.onBackPressed();
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
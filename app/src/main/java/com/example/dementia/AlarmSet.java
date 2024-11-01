package com.example.dementia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.Spinner;

//알람 생성 페이지에서 알람을 성정
public class AlarmSet extends AppCompatActivity {

    private ImageView pillImg;
    private Spinner ampm;
    private Spinner hour;
    private Spinner minute;
    private Button[] dayBtns;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmset);

        //변수 초기화 호출
        varInit();

        //Spinner 목록 불러오기
        initSpinner();

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

    private void convertPage(){
        Button cancel = findViewById(R.id.cancelAlarmBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setToListBack = new Intent(AlarmSet.this, AlarmList.class);
                finish();
                AlarmSet.super.onBackPressed();
            }
        });
    }
}
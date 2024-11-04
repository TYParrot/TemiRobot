package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dementia.R;

//알림 누르면 알림에 대한 정보를 불러와 이미지를 띄우며 복용 완료 버튼을 누르도록 함.
public class AlarmNotificationUI extends AppCompatActivity {

    private ImageView notiPillImg;
    private Button ateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notification);

        //변수 초기화 호출
        init();
    }

    //변수 초기화
    private void init(){
        notiPillImg = findViewById(R.id.alarmNotiPillImg);
        ateBtn = findViewById(R.id.atePillBtn);

        setNotiPillImg();
    }

    //사용자가 저장한 알약 이미지를 불러와서 세팅.
    private void setNotiPillImg(){

    }

    private void BtnClickEvent(){
        ateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dementia.R;

//알림 누르면 알림에 대한 정보를 불러와 이미지를 띄우며 복용 완료 버튼을 누르도록 함.
//Intent를 통해 알람 고유 ID 전달할 수 있도록 해야함.
public class AlarmNotificationUI extends AppCompatActivity {

    private ImageView notiPillImg;
    private Button ateBtn;
    private int notificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notification);

        //Intent로부터 알림 ID를 추출
        notificationID = getIntent().getIntExtra("alarm_id", -1);

        //변수 초기화 호출
        init();

        //버튼 클릭 이벤트 세팅
        BtnClickEvent();
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

    //버튼 클릭 이벤트
    private void BtnClickEvent(){
        ateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelNotification();

                System.out.println("ui에서 종료된 알람" + notificationID);
                finish();
            }
        });
    }

    //알림마다 각기 다른 고유의 id를 갖도록 생성하고, 이를 AlarmNotiUI페이지에 전달할 것.
    private void cancelNotification(){
        NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notiManager != null){
            notiManager.cancel(notificationID);
        }
    }
}
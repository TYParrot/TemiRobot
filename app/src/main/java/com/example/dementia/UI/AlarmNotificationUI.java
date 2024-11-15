package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dementia.R;

//알림 누르면 알림에 대한 정보를 불러와 이미지를 띄우며 복용 완료 버튼을 누르도록 함.
//Intent를 통해 알람 고유 ID 전달할 수 있도록 해야함.
public class AlarmNotificationUI extends AppCompatActivity {

    private ImageView notiPillImg;
    private Button ateBtn;
    private Button musicBtn;
    private int notificationID;
    private MediaPlayer m; // MediaPlayer를 멤버 변수로 선언

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
        musicBtn = findViewById(R.id.musicBtn);

        setNotiPillImg();

        // 음악 자동 재생
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(()->musicBtn.performClick(), 0);

        // 음악 버튼 클릭 리스너 설정
        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = view.getContext();
                m = MediaPlayer.create(c, R.raw.alarm_noti_cmajor); // 음악 파일 설정
                m.start(); // 음악 시작

                // 음악 종료 후 반복 재생 처리
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0); // 음악을 처음으로 되돌리고
                        mediaPlayer.start(); // 다시 시작
                    }
                });
            }
        });
    }

    //사용자가 저장한 알약 이미지를 불러와서 세팅.
    private void setNotiPillImg(){
        // 이미지 불러오기 설정 (필요한 코드 추가)
    }

    //버튼 클릭 이벤트
    private void BtnClickEvent(){
        ateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
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

    // Activity 종료 시 MediaPlayer 리소스 해제
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m != null) {
            m.stop(); // 음악 정지
            m.release(); // 리소스 해제
        }
    }
}

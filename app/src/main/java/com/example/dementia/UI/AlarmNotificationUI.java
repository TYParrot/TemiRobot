package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dementia.Function.AlarmListDataSet;
import com.example.dementia.Manager.AlarmDataManager;
import com.example.dementia.Manager.MainManager;
import com.example.dementia.R;

//알림 누르면 알림에 대한 정보를 불러와 이미지를 띄우며 복용 완료 버튼을 누르도록 함.
//Intent를 통해 알람 고유 ID 전달할 수 있도록 해야함.
public class AlarmNotificationUI extends AppCompatActivity {

    private AlarmDataManager alarmDataManager;
    private AlarmListDataSet alarmListDataSet;
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
        //클래스 초기화부터
        alarmDataManager = MainManager.getMain().getAlarm().getAlarmDataManager();

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
        alarmListDataSet = alarmDataManager.getAlarmById(notificationID);
        String notiImg = alarmListDataSet.getPillImgUri();

        if (notiImg != null) {
            // drawable 폴더에서 'pill1'이라는 이름의 리소스를 가져오기
            int resId = getResources().getIdentifier(notiImg, "drawable", getPackageName());

            if (resId != 0) {  // resId가 0이 아니면 유효한 리소스 ID가 있다는 뜻
                notiPillImg.setImageResource(resId);
            } else {
                notiPillImg.setImageResource(R.drawable.pill); // 기본 이미지 설정
            }
        } else {
            notiPillImg.setImageResource(R.drawable.pill); // 기본 이미지 설정
        }
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

    //사용자가 알림 소리를 들을 수 있도록
    //집 안을 한 바퀴 돌도록 하는 기능 추가 예정
    private void goAround(){

    }
}

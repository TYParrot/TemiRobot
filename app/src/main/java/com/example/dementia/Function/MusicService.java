package com.example.dementia.Function;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.dementia.R;

import java.lang.reflect.Field;

public class MusicService extends Service {

    private MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void onCreate(String mediaName) {
        super.onCreate();

        // 미디어 파일을 동적으로 로드
        int resId = getRawResourceId(mediaName);
        if (resId != 0) {
            mp = MediaPlayer.create(this, resId);
            mp.setLooping(false);  // 반복재생 설정
        } else {
            // 파일을 찾을 수 없으면 예외 처리
            // 필요에 따라 알림을 띄우거나 기본값을 설정
            System.out.println("배경음악을 찾을 수 없음");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 음악 시작
        if (mp != null) {
            mp.start();
        }
        // START_STICKY을 반환하여 서비스가 종료되지 않도록 설정
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 음악을 멈추고 리소스를 해제
        if (mp != null) {
            mp.stop();
            mp.release();  // 리소스 해제
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 바인딩 없이 사용할 경우 null 반환
        return null;
    }

    // 동적으로 R.raw 리소스 ID를 가져오는 메서드
    private int getRawResourceId(String mediaName) {
        try {
            // "raw"는 R.raw의 prefix임
            Field field = R.raw.class.getField(mediaName);
            return field.getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return 0;  // 리소스가 없으면 0 반환
        }
    }
}

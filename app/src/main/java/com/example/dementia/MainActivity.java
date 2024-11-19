package com.example.dementia;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import android.content.*;

import com.example.dementia.Manager.ImageUserManager;
import com.example.dementia.UI.ImageListUI;
import com.example.dementia.Manager.MainManager;
import com.example.dementia.UI.AlarmListUI;
import com.example.dementia.Manager.ImageUserManager;



public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 체크 호출, 어플 시작되고 나서 가장 먼저 확인해야하니까, 추가적인 기능은 이 아래에 작성해주세요.
        permissionCheck();

        //매니저들 초기화
        MainManager.getMain().initAlarmManager(this);
        MainManager.getMain().getAlarm().restoreAlarms();

        //버튼들의 화면 전환 메소드
        convertPage();

        //재시작시 알림 채널 재설정
    }



    // 권한 확인
    public void permissionCheck() {
        // 정확한 알람 설정 권한 확인 및 요청 (Android 12 이상)
        if (Build.VERSION.SDK_INT >= 31) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                // 사용자에게 설정 화면으로 안내
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            } else {
                // 정확한 알람 설정 권한이 허가됨
                // Toast.makeText(this, "정확한 알람 권한이 허가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허가됨
                Toast.makeText(this, "권한이 허가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 권한이 거부됨
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    * 화면 전환 버튼의 클릭 이벤트 설정하여 페이지를 변경하는 메소드.
    * onCreate에서 호출되고 있음.
    * */
    private void convertPage(){
//        이미지 자동 버튼
        Button imageBtn = findViewById(R.id.imageBtn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent images = new Intent(MainActivity.this, ImageListUI.class);
                startActivity(images);
            }
        });

        //알람 버튼
        Button alarmBtn = findViewById(R.id.alarmBtn);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alarmList = new Intent(MainActivity.this, AlarmListUI.class);
                startActivity(alarmList);
            }
        });

        //수면 버튼
//        Button sleepBtn = findViewById(R.id.sleepBtn);
//        sleepBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent sleep = new Intent(MainActivity.this, "여기 생성한 수면 페이지 클래스 넣으세요.");
//                startActivity(sleep);
//            }
//        });
    }


}

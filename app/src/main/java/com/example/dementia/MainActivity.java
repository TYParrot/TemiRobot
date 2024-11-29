////Temi 버전
//package com.example.dementia;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.AlarmManager;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.example.dementia.UI.StoryHobbyUI;
//import com.robotemi.sdk.Robot;
//import com.robotemi.sdk.permission.Permission;
//import com.robotemi.sdk.permission.OnRequestPermissionResultListener;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.dementia.Manager.MainManager;
//import com.example.dementia.UI.AlarmListUI;
//
//import java.util.Arrays;
//import java.util.List;
//
///* 추가해서 확인해야하는 것.
//* implements
//    Robot.NlpListener,
//    OnRobotReadyListener,
//    Robot.ConversationViewAttachesListener,
//    Robot.WakeupWordListener,
//    Robot.ActivityStreamPublishListener,
//    Robot.TtsListener,
//    OnBeWithMeStatusChangedListener,
//    OnGoToLocationStatusChangedListener,
//    OnLocationsUpdatedListener
//        * */
//
//public class MainActivity extends AppCompatActivity {
//
//    public static final int REQUEST_PERMISSION = 1;
//
//    /*
//    //temi 확인1
//    protected void onStart() {
//        super.onStart();
//        Robot.getInstance().addOnRobotReadyListener(this);
//        Robot.getInstance().addNlpListener(this);
//        Robot.getInstance().addOnBeWithMeStatusChangedListener(this);
//        Robot.getInstance().addOnGoToLocationStatusChangedListener(this);
//        Robot.getInstance().addConversationViewAttachesListenerListener(this);
//        Robot.getInstance().addWakeupWordListener(this);
//        Robot.getInstance().addTtsListener(this);
//        Robot.getInstance().addOnLocationsUpdatedListener(this);
//    }
//
//    protected void onStop() {
//        super.onStop();
//        Robot.getInstance().removeOnRobotReadyListener(this);
//        Robot.getInstance().removeNlpListener(this);
//        Robot.getInstance().removeOnBeWithMeStatusChangedListener(this);
//        Robot.getInstance().removeOnGoToLocationStatusChangedListener(this);
//        Robot.getInstance().removeConversationViewAttachesListenerListener(this);
//        Robot.getInstance().removeWakeupWordListener(this);
//        Robot.getInstance().removeTtsListener(this);
//        Robot.getInstance().removeOnLocationsUpdateListener(this);
//    }
//
//    @Override
//    public void onRobotReady(boolean isReady) {
//        if (isReady) {
//            try {
//                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
//                robot.onStart(activityInfo);
//            } catch (PackageManager.NameNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//*/
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Temi 권한 체크 및 요청
//        permissionCheck();
//
//        // 매니저 초기화
//        MainManager.getMain().initAlarmManager(this);
//        MainManager.getMain().getAlarm().restoreAlarms();
//        // 버튼들의 화면 전환 메소드
//        convertPage();
//    }
//
//    // Temi 권한 확인 및 요청
//    public void permissionCheck() {
//        // Temi SDK 권한 요청
//        List<Permission> permissions = Arrays.asList(Permission.FACE_RECOGNITION, Permission.MAP);
//        if (Robot.getInstance().checkSelfPermission(Permission.FACE_RECOGNITION) == Permission.DENIED) {
//            Robot.getInstance().requestPermissions(permissions, REQUEST_PERMISSION);
//        } else {
//            // 권한이 이미 부여됨
//            Toast.makeText(this, "권한이 허가되었습니다.", Toast.LENGTH_SHORT).show();
//        }
//
//        // 정확한 알람 설정 권한 확인 (Android 12 이상)
//        if (android.os.Build.VERSION.SDK_INT >= 31) {
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            if (!alarmManager.canScheduleExactAlarms()) {
//                // 사용자에게 설정 화면으로 안내
//                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                startActivity(intent);
//            } else {
//                // 정확한 알람 설정 권한이 허가됨
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 권한이 허가됨
//                Toast.makeText(this, "권한이 허가되었습니다.", Toast.LENGTH_SHORT).show();
//            } else {
//                // 권한이 거부됨
//                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // 화면 전환 버튼의 클릭 이벤트 설정하여 페이지를 변경하는 메소드
//    private void convertPage() {
//        // 이미지 자동 버튼
//        Button imageBtn = findViewById(R.id.imageBtn);
//        imageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent images = new Intent(MainActivity.this, com.example.dementia.UI.ImageDisplayUI.class);
//                startActivity(images);
//            }
//        });
//
//        // 알람 버튼
//        Button alarmBtn = findViewById(R.id.alarmBtn);
//        alarmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent alarmList = new Intent(MainActivity.this, AlarmListUI.class);
//                startActivity(alarmList);
//            }
//        });
//
//        // 수면 버튼 (수면 페이지를 추가할 경우)
//        // Button sleepBtn = findViewById(R.id.sleepBtn);
//        // sleepBtn.setOnClickListener(new View.OnClickListener() {
//        //     @Override
//        //     public void onClick(View view) {
//        //         Intent sleep = new Intent(MainActivity.this, SleepPage.class);
//        //         startActivity(sleep);
//        //     }
//        // });
//
//        //이야기 취미 버튼
//        Button storyHobbyBtn = findViewById(R.id.storyHobbyBtn);
//        storyHobbyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent storyHobby = new Intent(MainActivity.this, StoryHobbyUI.class);
//                startActivity(storyHobby);
//            }
//        });
//    }
//}


//안드로이드 버전
package com.example.dementia;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.AlarmManager;
        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.example.dementia.Manager.MainManager;
        import com.example.dementia.UI.AlarmListUI;
        import com.example.dementia.UI.StoryHobbyUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 매니저 초기화
        MainManager.getMain().initAlarmManager(this);
        MainManager.getMain().getAlarm().restoreAlarms();

        // 정확한 알람 설정 권한 확인 (Android 12 이상)
        permissionCheck();

        // 버튼들의 화면 전환 메소드
        convertPage();
    }

    // 정확한 알람 설정 권한 확인 (Android 12 이상)
    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 이상인지 확인
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                // 사용자에게 설정 화면으로 안내
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    // 화면 전환 버튼의 클릭 이벤트 설정하여 페이지를 변경하는 메소드
    private void convertPage() {
        // 이미지 자동 버튼
        Button imageBtn = findViewById(R.id.imageBtn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent images = new Intent(MainActivity.this, com.example.dementia.UI.ImageDisplayUI.class);
                startActivity(images);
            }
        });

        // 알람 버튼
        Button alarmBtn = findViewById(R.id.alarmBtn);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alarmList = new Intent(MainActivity.this, AlarmListUI.class);
                startActivity(alarmList);
            }
        });

        // 이야기 취미 버튼
        Button storyHobbyBtn = findViewById(R.id.storyHobbyBtn);
        storyHobbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storyHobby = new Intent(MainActivity.this, StoryHobbyUI.class);
                startActivity(storyHobby);
            }
        });
    }
}



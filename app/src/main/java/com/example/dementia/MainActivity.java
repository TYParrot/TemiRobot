package com.example.dementia;

//Temi 버전
import android.app.AlarmManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.Manager.MainManager;
import com.example.dementia.UI.AlarmListUI;
import com.example.dementia.UI.StoryHobbyUI;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.*;
import com.robotemi.sdk.permission.Permission;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener {

    private static final int REQUEST_PERMISSION = 1;
    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeManagers();
        checkPermissions();
        setupPageTransitions();
    }

    private void initializeManagers() {
        MainManager.getMain().initAlarmManager(this);
        MainManager.getMain().getAlarm().restoreAlarms();
    }

    private void checkPermissions() {
        checkTemiPermissions();
        checkAlarmPermission();
    }

    private void checkTemiPermissions() {
        List<Permission> permissions = Arrays.asList(Permission.FACE_RECOGNITION, Permission.MAP);
        robot = Robot.getInstance();

        if (robot.checkSelfPermission(Permission.FACE_RECOGNITION) == Permission.DENIED) {
            robot.requestPermissions(permissions, REQUEST_PERMISSION);
        } else {
            Toast.makeText(this, "권한이 허가되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    private void setupPageTransitions() {
        setupButton(R.id.imageBtn, com.example.dementia.UI.ImageDisplayUI.class);
        setupButton(R.id.alarmBtn, AlarmListUI.class);
        setupButton(R.id.storyHobbyBtn, StoryHobbyUI.class);
    }

    private void setupButton(int buttonId, Class<?> targetActivity) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(view -> {
                if (targetActivity != null) {
                    startActivity(new Intent(MainActivity.this, targetActivity));
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Robot robot = Robot.getInstance();
        robot.addOnRobotReadyListener(this);
        robot.addNlpListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addConversationViewAttachesListenerListener(this);
        robot.addWakeupWordListener(this);
        robot.addTtsListener(this);
        robot.addOnLocationsUpdatedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Robot robot = Robot.getInstance();
        robot.removeOnRobotReadyListener(this);
        robot.removeNlpListener(this);
        robot.removeOnBeWithMeStatusChangedListener(this);
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeConversationViewAttachesListenerListener(this);
        robot.removeWakeupWordListener(this);
        robot.removeTtsListener(this);
        //robot.removeOnLocationsUpdatedListener(this);
    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                Robot.getInstance().onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            String message = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ? "권한이 허가되었습니다."
                    : "권한이 거부되었습니다.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    // Override other Robot SDK callback methods as needed
    @Override public void onPublish(@NotNull ActivityStreamPublishMessage message) {}
    @Override public void onConversationAttaches(boolean isAttached) {}
    @Override public void onNlpCompleted(@NotNull NlpResult result) {}
    @Override public void onTtsStatusChanged(@NotNull TtsRequest request) {}
    @Override public void onWakeupWord(@NotNull String wakeupWord, int direction) {}
    @Override public void onBeWithMeStatusChanged(@NotNull String status) {}
    @Override public void onGoToLocationStatusChanged(@NotNull String location, @NotNull String status, int descriptionId, @NotNull String description) {}
    @Override public void onLocationsUpdated(@NotNull List<String> locations) {}
}


////안드로이드 버전
//package com.example.dementia;
//
//        import androidx.appcompat.app.AppCompatActivity;
//
//        import android.app.AlarmManager;
//        import android.content.Intent;
//        import android.os.Build;
//        import android.os.Bundle;
//        import android.provider.Settings;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.Toast;
//
//        import com.example.dementia.Manager.MainManager;
//        import com.example.dementia.UI.AlarmListUI;
//        import com.example.dementia.UI.StoryHobbyUI;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // 매니저 초기화
//        MainManager.getMain().initAlarmManager(this);
//        MainManager.getMain().getAlarm().restoreAlarms();
//
//        // 정확한 알람 설정 권한 확인 (Android 12 이상)
//        permissionCheck();
//
//        // 버튼들의 화면 전환 메소드
//        convertPage();
//    }
//
//    // 정확한 알람 설정 권한 확인 (Android 12 이상)
//    private void permissionCheck() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 이상인지 확인
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
//                // 사용자에게 설정 화면으로 안내
//                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                startActivity(intent);
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
//        // 이야기 취미 버튼
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
//
//

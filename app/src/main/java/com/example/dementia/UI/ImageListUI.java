package com.example.dementia.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.TimePickerDialog;
import android.widget.TextView;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.MainActivity;
import com.example.dementia.Manager.ImageUserManager;
import com.example.dementia.R;

import java.util.ArrayList;

public class ImageListUI extends AppCompatActivity {

    private Button backButton;
    private EditText nameInput;
    private Spinner soundEffectSpinner, userSpinner;
    private Button addUserButton, addImageButton;
    private ListView userListView, userImageListView; // 사용자 리스트 및 사용자-이미지 경로 리스트
    private TextView selectedSoundEffectText;
    private ImageUserManager imageUserManager;
    private Spinner intervalSpinner;
    private TextView startTimeText, endTimeText;
    private Button startTimeButton, endTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_set);

        imageUserManager = ImageUserManager.getInstance(this);

        // 사용자 관리 UI
        nameInput = findViewById(R.id.name_input);
        soundEffectSpinner = findViewById(R.id.soundEffectSpinner);
        addUserButton = findViewById(R.id.add_user_button);
        userListView = findViewById(R.id.user_list_view);

        // 이미지 설정 UI
        userSpinner = findViewById(R.id.userSpinner);
        selectedSoundEffectText = findViewById(R.id.selected_sound_effect);
        userImageListView = findViewById(R.id.user_image_list_view); // 사용자-이미지 경로 리스트

        // 효과음 드롭다운 설정
        ArrayAdapter<CharSequence> soundEffectAdapter = ArrayAdapter.createFromResource(this,
                R.array.sound_effects, android.R.layout.simple_spinner_item);
        soundEffectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundEffectSpinner.setAdapter(soundEffectAdapter);

        // 저장된 사용자 목록 표시
        updateUserListView();

        // 사용자 이름 목록을 Spinner에 표시
        updateUserSpinner();

        // 사용자-이미지 경로 목록 표시
        updateUserImageListView();

        //뒤로가기 버튼 클릭 시
        backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(v -> {
            // MainActivity로 돌아가기 위한 인텐트
            Intent intent = new Intent(ImageListUI.this, MainActivity.class);
            startActivity(intent);
            finish();  // 현재 Activity를 종료시켜 이전 화면으로 돌아가도록 처리
        });

        // 사용자 추가 버튼 클릭 시
        addUserButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String soundEffect = soundEffectSpinner.getSelectedItem().toString();

            if (!name.isEmpty()) {
                imageUserManager.saveUser(name, soundEffect);
                nameInput.setText(""); // 입력 필드 비우기
                updateUserListView(); // 사용자 목록 갱신
                updateUserSpinner();  // Spinner 갱신
                Toast.makeText(this, "사용자가 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }

        });

        // 사용자 선택 후 효과음 표시
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedUser = userSpinner.getSelectedItem().toString();
                String soundEffect = imageUserManager.getUserSoundEffect(selectedUser);
                selectedSoundEffectText.setText("선택된 효과음: " + soundEffect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때의 처리 (필요에 따라 추가)
            }
        }

        );

        // 이미지 추가하기 버튼 클릭 리스너
        addImageButton = findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(v -> {
            // 갤러리 열기
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 1); // 1은 요청 코드
        });

        // Section 3의 Spinner 설정
        intervalSpinner = findViewById(R.id.intervalSpinner);

        // 시간 간격 드롭다운 설정 (30, 60, 120분)
        ArrayAdapter<CharSequence> intervalAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"30분", "60분", "120분"});
        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(intervalAdapter);

        // 선택한 시간 간격에 대한 리스너
        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedInterval = intervalSpinner.getSelectedItem().toString();
                Toast.makeText(ImageListUI.this, "선택한 간격: " + selectedInterval, Toast.LENGTH_SHORT).show();
                // 선택한 값을 저장하는 로직 추가 가능
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때
            }
        });
        // 방해금지 시간 설정 UI
        startTimeText = findViewById(R.id.start_time_text);
        endTimeText = findViewById(R.id.end_time_text);
        startTimeButton = findViewById(R.id.start_time_button);
        endTimeButton = findViewById(R.id.end_time_button);

        // 시작 시간 설정 버튼 클릭 리스너
        startTimeButton.setOnClickListener(v -> showTimePickerDialog(startTimeText));

        // 종료 시간 설정 버튼 클릭 리스너
        endTimeButton.setOnClickListener(v -> showTimePickerDialog(endTimeText));
    }

    // 저장된 사용자 목록을 ListView에 표시
    private void updateUserListView() {
        ArrayList<String> users = imageUserManager.getAllUsers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, users);
        userListView.setAdapter(adapter);
    }

    // 저장된 사용자 목록을 Spinner에 표시
    private void updateUserSpinner() {
        ArrayList<String> users = imageUserManager.getAllUsers();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, users);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(spinnerAdapter);
    }

    // 저장된 사용자-이미지 경로 목록을 ListView에 표시
    private void updateUserImageListView() {
        ArrayList<String> users = imageUserManager.getAllUsers();
        ArrayList<String> userImageList = new ArrayList<>();

        for (String user : users) {
            String imagePath = imageUserManager.getUserImage(user);
            userImageList.add(user + " - " + (imagePath != null ? imagePath : null));
        }

        ArrayAdapter<String> imageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, userImageList);
        userImageListView.setAdapter(imageAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri selectedImageUri = data.getData();
            String selectedUser = userSpinner.getSelectedItem().toString();

            if (selectedImageUri != null && selectedUser != null) {
                imageUserManager.saveUserImage(selectedUser, selectedImageUri.toString());
                updateUserImageListView();  // 사용자-이미지 경로 목록 갱신
                Toast.makeText(this, "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // 시간 선택 다이얼로그 표시
    private void showTimePickerDialog(TextView timeTextView) {
        // 현재 시간을 기본값으로 설정
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // TimePickerDialog 생성
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    // 선택된 시간 설정
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeTextView.setText(timeTextView.getId() == R.id.start_time_text ? "시작 시간: " + time : "종료 시간: " + time);
                }, hour, minute, true);

        timePickerDialog.show();
    }

}
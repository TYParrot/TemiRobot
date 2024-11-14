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

import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.Manager.ImageUserManager;
import com.example.dementia.R;

import java.util.ArrayList;

public class ImageListUI extends AppCompatActivity {

    private EditText nameInput;
    private Spinner soundEffectSpinner, userSpinner;
    private Button addUserButton, addImageButton;
    private ListView userListView;
    private TextView selectedSoundEffectText;
    private ImageUserManager imageUserManager;

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

        // 효과음 드롭다운 설정
        ArrayAdapter<CharSequence> soundEffectAdapter = ArrayAdapter.createFromResource(this,
                R.array.sound_effects, android.R.layout.simple_spinner_item);
        soundEffectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundEffectSpinner.setAdapter(soundEffectAdapter);

        // 저장된 사용자 목록 표시
        updateUserListView();

        // 사용자 이름 목록을 Spinner에 표시
        updateUserSpinner();

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
        });

        // 이미지 추가하기 버튼 클릭 리스너
        addImageButton = findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(v -> {
            // 갤러리 열기
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 1); // 1은 요청 코드
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri selectedImageUri = data.getData();
            // 선택된 이미지를 처리하는 코드 추가 (예: 이미지 미리보기, 데이터 저장 등)
            Toast.makeText(this, "이미지가 선택되었습니다: " + selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

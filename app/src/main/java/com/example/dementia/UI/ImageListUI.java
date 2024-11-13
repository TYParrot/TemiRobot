package com.example.dementia.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.Manager.ImageUserManager;
import com.example.dementia.R;

import java.util.ArrayList;
import java.util.List;

public class ImageListUI extends AppCompatActivity {

    private Spinner soundEffectSpinner;
    private EditText nameInput;
    private Button addUserButton;
    private ListView userListView;
    private ArrayAdapter<String> userAdapter;
    private ArrayList<String> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_set); // 레이아웃 파일

        // ImageUserManager 인스턴스 가져오기
        ImageUserManager imageUserManager = ImageUserManager.getInstance(this);

        // 드롭다운(Spinner) 설정
        soundEffectSpinner = findViewById(R.id.soundEffectSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sound_effects, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundEffectSpinner.setAdapter(adapter);

        // 사용자 이름 입력 필드
        nameInput = findViewById(R.id.name_input);

        // 사용자 추가 버튼
        addUserButton = findViewById(R.id.add_user_button);

        // 사용자 목록 ListView 설정
        userListView = findViewById(R.id.user_list_view);
        userList = new ArrayList<>();
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(userAdapter);

        // 사용자 목록 업데이트
        updateUserList(imageUserManager);

        // 추가 버튼 클릭 시
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameInput.getText().toString();
                String selectedSoundEffect = soundEffectSpinner.getSelectedItem().toString();

                if (userName.isEmpty()) {
                    Toast.makeText(ImageListUI.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 사용자 정보 저장
                imageUserManager.saveUser(userName, selectedSoundEffect);

                // 추가 완료 메시지
                Toast.makeText(ImageListUI.this, "사용자가 추가되었습니다.", Toast.LENGTH_SHORT).show();

                // 입력 필드 초기화
                nameInput.setText("");
                soundEffectSpinner.setSelection(0); // 첫 번째 항목 선택

                // 사용자 목록 업데이트
                updateUserList(imageUserManager);
            }
        });
    }

    // 사용자 목록 업데이트 메서드
    private void updateUserList(ImageUserManager imageUserManager) {
        List<String> usersWithEffects = imageUserManager.getAllUsersWithEffects(); // 사용자 이름과 효과음 가져오기
        userList.clear(); // 기존 목록 초기화
        userList.addAll(usersWithEffects); // 새로운 목록 추가
        userAdapter.notifyDataSetChanged(); // 어댑터에 변경 사항 알리기
    }
}

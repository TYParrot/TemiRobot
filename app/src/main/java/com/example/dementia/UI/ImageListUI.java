package com.example.dementia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.R;
import com.example.dementia.Manager.ImageUserManager;

import java.util.List;

public class ImageListUI extends AppCompatActivity {

    private ListView imageListView;
    private ArrayAdapter<String> imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_set);

        imageListView = findViewById(R.id.imageListView); // ListView는 xml에 추가되어 있어야 합니다.
        loadImageList();


        // 뒤로가기 버튼 클릭
        Button backBtn = findViewById(R.id.backBtn); // 돌아가기 버튼을 위한 ID 설정
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // 현재 Activity 종료
            }
        });
    }

    // 이미지 목록 불러오기
    private void loadImageList() {
        ImageUserManager imageUserManager = ImageUserManager.getInstance(this);
        List<String> imageList = imageUserManager.getImageList();

        imageListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, imageList);
        imageListView.setAdapter(imageListAdapter);
    }
}

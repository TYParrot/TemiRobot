package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.dementia.R;

public class StoryHobbyUI extends AppCompatActivity {

    private Button storyBtn;
    private Button hobbyBtn;
    private Button funBtn;
    private Button sadBtn;
    private Button scienceBtn;
    private Button shortTime;
    private Button longTime;
    private Button longDay;
    private LinearLayout storyLay;
    private LinearLayout hobbyLay;

    
    //GPT에 전달할 태그 값
    private String layType;
    private String genreType;
    private String timeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyhobby);

        initBtn();


    }

    //버튼 초기화
    private void initBtn(){
        storyBtn = findViewById(R.id.storyBtn);
        hobbyBtn = findViewById(R.id.hobbyBtn);
        funBtn = findViewById(R.id.fun);
        sadBtn = findViewById(R.id.sad);
        scienceBtn = findViewById(R.id.science);
        shortTime = findViewById(R.id.shortTime);
        longTime = findViewById(R.id.longTime);
        longDay = findViewById(R.id.longDay);

        storyLay = findViewById(R.id.storyLayout);
        hobbyLay = findViewById(R.id.hobbyLayout);

    }

    private void clickBtn(){
        //이야기 선택
        storyBtn.setOnClickListener(view -> {
            layType = "story";

            storyLay.setVisibility(View.VISIBLE);
            hobbyLay.setVisibility(View.GONE);

            // 이야기 버튼과 취미 버튼을 숨김
            storyBtn.setVisibility(View.GONE);
            hobbyBtn.setVisibility(View.GONE);
        });

        //취미 선택
        hobbyBtn.setOnClickListener(view -> {
            layType = "hobby";

            storyLay.setVisibility(View.GONE);
            hobbyLay.setVisibility(View.VISIBLE);

            // 이야기 버튼과 취미 버튼을 숨김
            storyBtn.setVisibility(View.GONE);
            hobbyBtn.setVisibility(View.GONE);
        });

        //세부 태그 버튼
        funBtn.setOnClickListener(view -> {
            genreType = "fun";
        });

        sadBtn.setOnClickListener(view -> {
            genreType = "sad";
        });

        scienceBtn.setOnClickListener(view -> {
            genreType = "science";
        });

        shortTime.setOnClickListener(view -> {
            timeType = "shortTime";
        });

        longTime.setOnClickListener(view->{
            timeType = "longTime";
        });

        longDay.setOnClickListener(view -> {
            timeType = "longDay";
        });
    }
}
package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dementia.Function.ChatGPTRequest;
import com.example.dementia.Function.ChatGPTResponse;
import com.example.dementia.Function.GPTChat;
import com.example.dementia.Function.GPTChatInterface;
import com.example.dementia.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//사용자 응답에 따른 GPT 요구
public class StoryHobbyUI extends AppCompatActivity {

    private Button storyBtn;
    private Button hobbyBtn;
    private Button funBtn;
    private Button sadBtn;
    private Button scienceBtn;
    private Button shortTime;
    private Button longTime;
    private Button longDay;
    private Button backBtn;
    private LinearLayout storyLay;
    private LinearLayout hobbyLay;
    private TextView resultTextView;


    
    //GPT에 전달할 태그 값
    private String layType;
    private String genreType;
    private String timeType;

    private GPTChatInterface gptChatInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyhobby);

        initApiService();
        initBtn();
        clickBtn();


    }

    //GPTChat static이므로 객체 생성 없이 호출
    private void initApiService() {
        gptChatInterface = GPTChat.getChatGPTApi();
    }

    private void sendMessageToGpt(String messageContent){
        ChatGPTRequest.Messages msg = new ChatGPTRequest.Messages("user", messageContent);
        ChatGPTRequest request = new ChatGPTRequest("gpt-3.5-turbo", Collections.singletonList(msg));

        gptChatInterface.getChatResponse(request).enqueue(new Callback<ChatGPTResponse>() {
            @Override
            public void onResponse(Call<ChatGPTResponse> call, Response<ChatGPTResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    String getResponse = response.body().getChoices().get(0).getMessage().getContent();

                    //gpt가 만든 결과 출력
                    System.out.println(getResponse);
                    resultTextView.setText(getResponse);
                    resultTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ChatGPTResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void makeSentence() {
        String sentence = ""; // 결과 문장을 저장할 변수

        // 현재 날짜와 시간 가져오기
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN);
        SimpleDateFormat timeFormat = new SimpleDateFormat("a h시 mm분", Locale.KOREAN);
        String currentDate = dateFormat.format(now);
        String currentTime = timeFormat.format(now);

        if (layType.equals("story")) {
            if (genreType != null) {
                // 이야기 관련 문장 생성
                sentence = currentDate + " " + currentTime + "에 어울리는 " + genreType + " 이야기를 들려주세요.";
            } else {
                // 장르가 선택되지 않았을 경우
                sentence = "듣고 싶은 이야기의 종류를 선택해주세요.";
            }
        } else if (layType.equals("hobby")) {
            if (timeType != null) {
                // 취미 관련 문장 생성
                sentence = currentDate + " " + currentTime + "에 어울리는 " + timeType + " 동안 할 수 있는 취미를 추천해주세요.";
            } else {
                // 시간이 선택되지 않았을 경우
                sentence = "취미 활동의 시간을 선택해주세요.";
            }
        } else {
            // layType이 설정되지 않은 경우
            sentence = "이야기 또는 취미 중 하나를 선택해주세요.";
        }

        // 보내는 메세지를 로그로 출력하거나 GPT API 호출 메서드로 전달
        System.out.println(sentence);
        sendMessageToGpt(sentence); // GPT API 호출 메서드
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
        backBtn = findViewById(R.id.storyHobbyBackBtn);

        storyLay = findViewById(R.id.storyLayout);
        hobbyLay = findViewById(R.id.hobbyLayout);

        resultTextView = findViewById(R.id.resultTextView);

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
            storyLay.setVisibility(View.GONE);
        });

        sadBtn.setOnClickListener(view -> {
            genreType = "sad";
            storyLay.setVisibility(View.GONE);
        });

        scienceBtn.setOnClickListener(view -> {
            genreType = "science";
            storyLay.setVisibility(View.GONE);
        });

        shortTime.setOnClickListener(view -> {
            timeType = "shortTime";
            hobbyLay.setVisibility(View.GONE);
        });

        longTime.setOnClickListener(view->{
            timeType = "longTime";
            hobbyLay.setVisibility(View.GONE);
        });

        longDay.setOnClickListener(view -> {
            timeType = "longDay";
            hobbyLay.setVisibility(View.GONE);
        });

        //종료하기
        storyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                StoryHobbyUI.super.onBackPressed();
            }
        });
    }
}
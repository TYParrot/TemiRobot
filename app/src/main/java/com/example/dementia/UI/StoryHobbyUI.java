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

        // 초기 상태 설정
        storyLay.setVisibility(View.GONE);
        hobbyLay.setVisibility(View.GONE);
    }

    private void initApiService() {
        gptChatInterface = GPTChat.getChatGPTApi();
    }

    private void sendMessageToGpt(String messageContent){
        ChatGPTRequest.Messages msg = new ChatGPTRequest.Messages("user", messageContent);
        ChatGPTRequest request = new ChatGPTRequest("gpt-3.5-turbo", Collections.singletonList(msg));

        gptChatInterface.getChatResponse(request).enqueue(new Callback<ChatGPTResponse>() {
            @Override
            public void onResponse(Call<ChatGPTResponse> call, Response<ChatGPTResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getChoices() != null) {
                    String getResponse = response.body().getChoices().get(0).getMessage().getContent();
                    resultTextView.setText(getResponse);
                    resultTextView.setVisibility(View.VISIBLE);
                    System.out.println(getResponse);
                } else {
                    System.out.println("No choices or null response body.");
                }
            }

            @Override
            public void onFailure(Call<ChatGPTResponse> call, Throwable t) {
                t.printStackTrace();
                System.out.println("API Call Failure: " + t.getMessage()); // 에러 메시지 출력
            }
        });
    }

    private void makeSentence() {
        String sentence = "";
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN);
        SimpleDateFormat timeFormat = new SimpleDateFormat("a h시 mm분", Locale.KOREAN);
        String currentDate = dateFormat.format(now);
        String currentTime = timeFormat.format(now);

        if (layType.equals("story")) {
            if (genreType != null) {
                sentence = currentDate + " " + currentTime + "에 어울리는 " + genreType + " 이야기를 들려주세요.";
            } else {
                sentence = "듣고 싶은 이야기의 종류를 선택해주세요.";
            }
        } else if (layType.equals("hobby")) {
            if (timeType != null) {
                sentence = currentDate + " " + currentTime + "에 어울리는 " + timeType + " 동안 할 수 있는 취미를 추천해주세요.";
            } else {
                sentence = "취미 활동의 시간을 선택해주세요.";
            }
        } else {
            sentence = "이야기 또는 취미 중 하나를 선택해주세요.";
        }

        System.out.println(sentence);
        sendMessageToGpt(sentence);
    }

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
        storyBtn.setOnClickListener(view -> {
            layType = "story";
            storyLay.setVisibility(View.VISIBLE);
            hobbyLay.setVisibility(View.GONE);
            storyBtn.setVisibility(View.GONE);
            hobbyBtn.setVisibility(View.GONE);
        });

        hobbyBtn.setOnClickListener(view -> {
            layType = "hobby";
            storyLay.setVisibility(View.GONE);
            hobbyLay.setVisibility(View.VISIBLE);
            storyBtn.setVisibility(View.GONE);
            hobbyBtn.setVisibility(View.GONE);
        });

        funBtn.setOnClickListener(view -> {
            genreType = "fun";
            storyLay.setVisibility(View.GONE);
            makeSentence();
        });

        sadBtn.setOnClickListener(view -> {
            genreType = "sad";
            storyLay.setVisibility(View.GONE);
            makeSentence();
        });

        scienceBtn.setOnClickListener(view -> {
            genreType = "science";
            storyLay.setVisibility(View.GONE);
            makeSentence();
        });

        shortTime.setOnClickListener(view -> {
            timeType = "shortTime";
            hobbyLay.setVisibility(View.GONE);
            makeSentence();
        });

        longTime.setOnClickListener(view -> {
            timeType = "longTime";
            hobbyLay.setVisibility(View.GONE);
            makeSentence();
        });

        longDay.setOnClickListener(view -> {
            timeType = "longDay";
            hobbyLay.setVisibility(View.GONE);
            makeSentence();
        });

        // 뒤로가기 버튼
        backBtn.setOnClickListener(view -> finish());
    }
}

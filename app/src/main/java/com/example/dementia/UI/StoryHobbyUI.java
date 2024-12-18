package com.example.dementia.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.dementia.Function.ChatGPTRequest;
import com.example.dementia.Function.ChatGPTResponse;
import com.example.dementia.Function.GPTChat;
import com.example.dementia.Function.GPTChatInterface;
import com.example.dementia.MainActivity;
import com.example.dementia.R;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

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
    private ScrollView resultScrollView;

    private String layType;
    private String genreType;
    private String timeType;

    private GPTChatInterface gptChatInterface;

    private Robot robot;

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

        robot = Robot.getInstance();
    }

    private void initApiService() {
        gptChatInterface = GPTChat.getChatGPTApi();
    }

    private void sendMessageToGpt(String messageContent) {
        // 사용자 메시지 준비
        ChatGPTRequest.Messages msg = new ChatGPTRequest.Messages("user", messageContent);
        // ChatGPTRequest 생성
        ChatGPTRequest request = new ChatGPTRequest("gpt-4o-mini", Collections.singletonList(msg));

        // API 호출
        gptChatInterface.getChatResponse(request).enqueue(new Callback<ChatGPTResponse>() {
            @Override
            public void onResponse(Call<ChatGPTResponse> call, Response<ChatGPTResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 응답이 성공적인 경우, choices 필드가 null이 아니고 내용이 있는지 확인
                    if (response.body().getChoices() != null && !response.body().getChoices().isEmpty()) {
                        String getResponse = response.body().getChoices().get(0).getMessage().getContent();
                        // 응답 내용 UI에 표시
                        resultTextView.setText(getResponse);
                        resultScrollView.setVisibility(View.VISIBLE);
                        System.out.println("Response: " + getResponse);

                        //Temi가 읽어줌.
                        robot.speak(TtsRequest.create(getResponse, false));

                    } else {
                        // choices가 null이거나 비어있는 경우 처리
                        System.out.println("No choices or null response body.");
                    }
                } else {
                    // API 호출이 성공적이지 않은 경우 상태 코드와 메시지 로그 출력
                    System.out.println("API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ChatGPTResponse> call, Throwable t) {
                // 실패한 경우 에러 로그 출력
                t.printStackTrace();
                System.out.println("API Call Failure: " + t.getMessage());
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
                sentence = "다음의 규칙을 지켜주세요. 1. 날짜, 시간 및 마크 다운과 관련된 내용은 빼주세요. " +
                        "2. 사회복지사처럼 친근한 말투로 자연스럽게 말해주세요.. 3. 최대 2개만 추천해주세요." +currentDate + " " + currentTime + "에 어울리는 " + genreType + " 이야기를 들려주세요.";
            } else {
                sentence = "다음의 규칙을 지켜주세요. 1. 날짜, 시간 및 마크 다운과 관련된 내용은 빼주세요. " +
                        "2. 사회복지사처럼 친근한 말투로 자연스럽게 말해주세요. 3. 최대 2개만 추천해주세요." +
                        "즐거운 이야기를 말해주세요.";
            }
        } else if (layType.equals("hobby")) {
            if (timeType != null) {
                sentence = "다음의 규칙을 지켜주세요. 1. 날짜, 시간 및 마크 다운과 관련된 내용은 빼주세요. " +
                        "2. 사회복지사처럼 친근한 말투로 자연스럽게 말해주세요. 3. 최대 2개만 추천해주세요" + currentDate + " " + currentTime + "에 어울리는 " + timeType + " 동안 할 수 있는 취미를 추천해주세요.";
            } else {
                sentence = "다음의 규칙을 지켜주세요. 1. 날짜, 시간 및 마크 다운과 관련된 내용은 빼주세요." +
                        "2. 사회복지사처럼 친근한 말투로 자연스럽게 말해주세요. 3. 최대 2개만 추천해주세요" +
                        "아무 시간대의 취미를 추천해주세요.";
            }
        } else {
            sentence = "다음의 규칙을 지켜주세요. 1. 날짜, 시간 및 마크 다운과 관련된 내용은 빼주세요. " +
                    "2. 사회복지사처럼 친근한 말투로 자연스럽게 말해주세요.. 3. 최대 2개만 추천해주세요.";
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
        resultScrollView = findViewById(R.id.resultScrollView);
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
        backBtn.setOnClickListener(view -> {
            robot.cancelAllTtsRequests();
            finish();
        });
    }
}

package com.example.dementia.Function;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GPTChatInterface {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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"  // api키는 Bearer 뒤에 공백 한칸 띄고 입력합니다.
    })
    @POST("chat/completions")
    Call<ChatGPTResponse> getChatResponse(@Body ChatGPTRequest request);

}


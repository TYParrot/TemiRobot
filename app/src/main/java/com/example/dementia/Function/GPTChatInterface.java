package com.example.dementia.Function;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GPTChatInterface {

    @POST("chat/completions")
    Call<ChatGPTResponse> getChatResponse(@Body ChatGPTRequest request);

}


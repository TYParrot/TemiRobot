package com.example.dementia.Function;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GPTChat {
    private static final String BASE_URL = "https://api.openai.com/";

    public static ChatGPTApi getChatGPTApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ChatGPTApi.class);
    }

}


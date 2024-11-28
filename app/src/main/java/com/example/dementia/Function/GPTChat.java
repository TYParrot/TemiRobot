package com.example.dementia.Function;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GPTChat {
    private static final String BASE_URL = "https://api.openai.com/";
    private static GPTChatInterface gptApi;

    public static GPTChatInterface getChatGPTApi(){
        if(gptApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gptApi = retrofit.create(GPTChatInterface.class);
        }
        return gptApi;
    }


}


package com.example.dementia.Function;

import com.example.dementia.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Interceptor;
import okhttp3.Response;
import java.io.IOException;

public class GPTChat {
    private static final String BASE_URL = "https://api.openai.com/v1/";
    private static GPTChatInterface gptApi;

    public static GPTChatInterface getChatGPTApi(){
        if(gptApi == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer "+ BuildConfig.GPT_API_KEY)
                                    .build();
                            System.out.println("API_REQUEST " + "Request Headers: " + newRequest.headers());
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)  // 클라이언트 설정
                    .build();

            gptApi = retrofit.create(GPTChatInterface.class);
        }
        return gptApi;
    }
}



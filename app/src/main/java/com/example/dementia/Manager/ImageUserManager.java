package com.example.dementia.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageUserManager {

    private static ImageUserManager instance;
    private SharedPreferences sharedPreferences;

    private ImageUserManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    public static ImageUserManager getInstance(Context context) {
        if (instance == null) {
            instance = new ImageUserManager(context);
        }
        return instance;
    }

    // 사용자 이름과 선택된 효과음을 저장
    public void saveUser(String name, String soundEffect) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, soundEffect);
        editor.apply();
    }

    // 저장된 사용자 이름과 효과음 불러오기
    public String getUserSoundEffect(String name) {
        return sharedPreferences.getString(name, "기본 효과음");
    }

    // 저장된 사용자 삭제
    public void deleteUser(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.apply();
    }

    // 모든 사용자 이름과 효과음을 가져오기
    public List<String> getAllUsersWithEffects() {
        List<String> usersWithEffects = new ArrayList<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();

        // 모든 사용자 이름과 그에 해당하는 효과음을 리스트에 추가
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String userName = entry.getKey();
            String soundEffect = (String) entry.getValue();
            usersWithEffects.add(userName + " - " + soundEffect); // 이름과 효과음을 하나의 문자열로 묶기
        }

        return usersWithEffects;
    }
}

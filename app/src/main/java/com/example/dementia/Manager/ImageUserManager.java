package com.example.dementia.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
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
        editor.putString("sound_" + name, soundEffect);  // 효과음을 "sound_" 접두사로 저장
        editor.apply();
    }

    // 사용자 이름과 이미지 경로를 저장
    public void saveUserImage(String name, String imagePath) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image_" + name, imagePath);  // 이미지 경로를 "image_" 접두사로 저장
        editor.apply();
    }

    // 저장된 사용자 이름의 효과음 가져오기
    public String getUserSoundEffect(String name) {
        return sharedPreferences.getString("sound_" + name, "기본 효과음");
    }

    // 저장된 사용자 이름의 이미지 경로 가져오기
    public String getUserImage(String name) {
        return sharedPreferences.getString("image_" + name, null);
    }

    // 저장된 사용자 목록 가져오기
    public ArrayList<String> getAllUsers() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        ArrayList<String> users = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            // "sound_" 접두사를 통해 사용자 이름을 구별하여 추가
            if (key.startsWith("sound_")) {
                String name = key.substring("sound_".length());
                users.add(name);
            }
        }
        return users;
    }

    // 저장된 사용자 삭제 (효과음과 이미지 경로 모두 삭제)
    public void deleteUser(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("sound_" + name);  // 효과음 삭제
        editor.remove("image_" + name);  // 이미지 경로 삭제
        editor.apply();
    }
}

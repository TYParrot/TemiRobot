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
        editor.putString(name, soundEffect);
        editor.apply();
    }

    // 저장된 사용자 이름과 효과음 불러오기
    public String getUserSoundEffect(String name) {
        return sharedPreferences.getString(name, "기본 효과음");
    }

    // 저장된 사용자 목록 가져오기
    public ArrayList<String> getAllUsers() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        ArrayList<String> users = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            users.add(entry.getKey()); // 사용자 이름은 key에 저장되어 있음
        }
        return users;
    }

    // 저장된 사용자 삭제
    public void deleteUser(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.apply();
    }
}

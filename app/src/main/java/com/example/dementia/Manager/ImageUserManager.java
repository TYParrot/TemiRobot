package com.example.dementia.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class ImageUserManager {

    private static final String PREF_NAME = "ImageUserPreferences";
    private static final String IMAGE_LIST_KEY = "ImageList"; // 이미지 목록을 저장할 키

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Singleton 패턴
    private static ImageUserManager instance;

    private ImageUserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static ImageUserManager getInstance(Context context) {
        if (instance == null) {
            instance = new ImageUserManager(context);
        }
        return instance;
    }

    // 이미지 목록 저장
    public void saveImageList(List<String> imagePaths) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String path : imagePaths) {
            stringBuilder.append(path).append(",");
        }
        editor.putString(IMAGE_LIST_KEY, stringBuilder.toString());
        editor.apply();
    }

    // 이미지 목록 불러오기
    public List<String> getImageList() {
        List<String> imageList = new ArrayList<>();
        String savedImages = sharedPreferences.getString(IMAGE_LIST_KEY, "");
        if (!savedImages.isEmpty()) {
            String[] imagePaths = savedImages.split(",");
            for (String path : imagePaths) {
                if (!path.isEmpty()) {
                    imageList.add(path);
                }
            }
        }
        return imageList;
    }

    // 이미지 추가
    public void addImage(String imagePath) {
        List<String> currentList = getImageList();
        currentList.add(imagePath);
        saveImageList(currentList); // 수정된 리스트 저장
    }

    // 이미지 삭제
    public void deleteImage(String imagePath) {
        List<String> currentList = getImageList();
        if (currentList.contains(imagePath)) {
            currentList.remove(imagePath);
            saveImageList(currentList); // 수정된 리스트 저장
        }
    }
}

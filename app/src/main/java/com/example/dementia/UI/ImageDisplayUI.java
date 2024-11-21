package com.example.dementia.UI;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.MainActivity;
import com.example.dementia.R;

public class ImageDisplayUI extends AppCompatActivity {

    private ImageView imageView;
    private Button imgeBackBtn;
    private Handler handler = new Handler();

    private int currentIndex = 0;
    private int currentGroup = 0; // 0: daughter, 1: son, 2: family

    private int[][] images = {
            {R.drawable.daughter1, R.drawable.daughter2, R.drawable.daughter3, R.drawable.daughter4, R.drawable.daughter5},
            {R.drawable.son1, R.drawable.son2, R.drawable.son3, R.drawable.son4, R.drawable.son5},
            {R.drawable.family1, R.drawable.family2, R.drawable.family3, R.drawable.family4, R.drawable.family5}
    };

    private int[] sounds = {
            R.raw.daughter_sound, // Replace with actual sound files
            R.raw.son_sound,
            R.raw.family_sound
    };

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        imageView = findViewById(R.id.imageView);
        imgeBackBtn = findViewById(R.id.imgBackBtn);

        imgeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setToListBack = new Intent(ImageDisplayUI.this, MainActivity.class);
                finish();
                ImageDisplayUI.super.onBackPressed();
            }
        });

        startSlideshow();
    }

    private void startSlideshow() {
        playSound();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(images[currentGroup][currentIndex]);
                currentIndex = (currentIndex + 1) % images[currentGroup].length;

                if (currentIndex == 0) { // Group change
                    currentGroup = (currentGroup + 1) % images.length;
                    playSound();
                }

                handler.postDelayed(this, 5000); // 5 seconds delay
            }
        }, 0);
    }

    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, sounds[currentGroup]);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}

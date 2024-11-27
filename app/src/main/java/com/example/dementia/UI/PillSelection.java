package com.example.dementia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dementia.R;

public class PillSelection extends AppCompatActivity {

    private ImageView pill1Img, pill2Img, pill3Img, pill4Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_selection);

        // ImageView 초기화
        pill1Img = findViewById(R.id.pill1Img);
        pill2Img = findViewById(R.id.pill2Img);
        pill3Img = findViewById(R.id.pill3Img);
        pill4Img = findViewById(R.id.pill4Img);

        // 각 이미지뷰에 이미지를 설정
        pill1Img.setImageResource(R.drawable.pill1);
        pill2Img.setImageResource(R.drawable.pill2);
        pill3Img.setImageResource(R.drawable.pill3);
        pill4Img.setImageResource(R.drawable.pill4);

        // 클릭 이벤트 설정
        pill1Img.setOnClickListener(v -> returnSelectedPill("pill1"));
        pill2Img.setOnClickListener(v -> returnSelectedPill("pill2"));
        pill3Img.setOnClickListener(v -> returnSelectedPill("pill3"));
        pill4Img.setOnClickListener(v -> returnSelectedPill("pill4"));
    }

    /**
     * 선택한 알약 이미지를 반환
     *
     * @param selectedPill 선택된 알약의 리소스 이름
     */
    private void returnSelectedPill(String selectedPill) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedPill", selectedPill); // 결과 데이터를 담아 전달
        setResult(RESULT_OK, resultIntent); // 결과 코드와 데이터 설정
        finish(); // 액티비티 종료
    }
}

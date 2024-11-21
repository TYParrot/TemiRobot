package com.example.dementia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dementia.R;

public class PillSelection extends AppCompatActivity {

    private GridView pillGridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_selection);

        pillGridView = findViewById(R.id.pillGridView);

        // 문자열 배열 (이미지 이름 또는 ID를 담은 배열)
        String[] pillNames = {"pill1", "pill2", "pill3","pill4"};

        // 커스텀 어댑터 설정
        imageAdapter = new ImageAdapter(pillNames);
        pillGridView.setAdapter(imageAdapter);

        pillGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 선택된 이미지의 이름을 반환
                String selectedPill = pillNames[i];
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPill", selectedPill);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    // 커스텀 이미지 어댑터
    private class ImageAdapter extends BaseAdapter {
        private final String[] pillNames;

        public ImageAdapter(String[] pillNames) {
            this.pillNames = pillNames;
        }

        @Override
        public int getCount() {
            return pillNames.length;
        }

        @Override
        public Object getItem(int position) {
            return pillNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(PillSelection.this);
                imageView.setLayoutParams(new GridView.LayoutParams(
                        GridView.LayoutParams.MATCH_PARENT,  // 너비
                        400));  // 높이
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                imageView = (ImageView) convertView;
            }

            // 리소스 ID 가져오기
            int resId = getResources().getIdentifier(pillNames[position], "drawable", getPackageName());
            Log.d("PillSelection", "Resource ID for " + pillNames[position] + ": " + resId);

            if (resId != 0) {
                imageView.setImageResource(resId);
            } else {
                Log.e("PillSelection", "Invalid resource ID for " + pillNames[position]);
                imageView.setImageResource(R.drawable.pill); // 기본 이미지
            }

            return imageView;
        }

    }
}

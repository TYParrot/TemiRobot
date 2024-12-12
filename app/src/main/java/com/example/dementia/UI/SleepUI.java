package com.example.dementia.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dementia.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

public class SleepUI extends AppCompatActivity {

    private TextView sensorTextview1, sensorTextview2, sensorTextview3, sensorTextview4, sensorTextview5, sensorTextview6;
    private Button startBtn, stopBtn, cancelSleep;
    private LineChart lineChart;

    private int[] pressCounts = new int[6]; // 센서별 누름 횟수
    private String[] states = new String[6]; // 센서별 상태 (LIGHT/DEEP)

    // FirebaseDatabase 객체 생성
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference[] sensorRefs = {
            database.getReference("SENSOR1"),
            database.getReference("SENSOR2"),
            database.getReference("SENSOR3"),
            database.getReference("SENSOR4"),
            database.getReference("SENSOR5"),
            database.getReference("SENSOR6")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep); // Use the new XML layout (if you have one)

        initializeViews();
        setupSensorListeners();
        setupLineChart();
        setupButtonListeners();
    }

    // View 초기화 메서드
    private void initializeViews() {
        sensorTextview1 = findViewById(R.id.sensorTextview1);
        sensorTextview2 = findViewById(R.id.sensorTextview2);
        sensorTextview3 = findViewById(R.id.sensorTextview3);
        sensorTextview4 = findViewById(R.id.sensorTextview4);
        sensorTextview5 = findViewById(R.id.sensorTextview5);
        sensorTextview6 = findViewById(R.id.sensorTextview6);

        startBtn = findViewById(R.id.startBtn);
        stopBtn = findViewById(R.id.stopBtn);
        cancelSleep = findViewById(R.id.cancel_sleep);
        lineChart = findViewById(R.id.lineChart);
    }

    // 그래프 설정
    private void setupLineChart() {
        lineChart.getDescription().setEnabled(false);

        // XAxis 설정: 30초 간격으로 시간 표시
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);  // X축을 1 간격으로 설정
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(10, true); // 10개의 레이블 표시

        // X축에서 0부터 시작하도록 설정
        xAxis.setAxisMinimum(0f);  // X축이 0부터 시작하도록 설정

        // YAxis 설정: 정수 값만 표시하고, 0부터 시작하도록 설정
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(1f);  // Y축 값이 정수로 나오도록 설정
        leftAxis.setAxisMinimum(0f);  // Y축이 0부터 시작하도록 설정

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);  // 오른쪽 Y축 숨기기
    }

    // 센서 리스너 설정
    private void setupSensorListeners() {
        TextView[] sensorTextViews = {
                sensorTextview1, sensorTextview2, sensorTextview3, sensorTextview4, sensorTextview5, sensorTextview6
        };

        for (int i = 0; i < sensorRefs.length; i++) {
            int finalI = i;
            sensorRefs[i].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object sensorData = dataSnapshot.getValue();
                    if (sensorData != null) {
                        try {
                            long sensorValue = (Long) sensorData;

                            // 상태 업데이트
                            updatePressCounts(finalI, sensorValue);

                            // UI 업데이트
                            runOnUiThread(() -> {
                                updateSensorColor(sensorValue > 500, sensorTextViews[finalI]);
                                if (isRunning) {
                                    updateGraph();
                                }
                            });
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "센서 데이터 형식 오류", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "센서 데이터가 비어 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 센서 값에 따라 누름 횟수 및 상태 업데이트
    private void updatePressCounts(int sensorIndex, long sensorValue) {
        // 센서 값이 500 이상일 때만 누름 횟수 증가
        if (sensorValue > 500) {
            pressCounts[sensorIndex]++;
        }

        // 누름 횟수가 10회 이상이면 LIGHT 상태로 변경
        if (pressCounts[sensorIndex] >= 10) {
            states[sensorIndex] = "LIGHT";
        } else {
            states[sensorIndex] = "DEEP";
        }
    }

    // 센서 텍스트뷰 색상 업데이트
    private void updateSensorColor(boolean isPressed, TextView sensorTextView) {
        if (sensorTextView != null) {
            if (isPressed) {
                sensorTextView.setBackgroundColor(Color.RED);  // 값이 500 이상일 때 빨간색
            } else {
                sensorTextView.setBackgroundColor(Color.WHITE);  // 기본값 (흰색)
            }
        }
    }

    private boolean isRunning = false;  // 동작 여부 상태
    private long startTime = 0;  // 시작 시간
    private long lastUpdateTime = 0;  // 마지막 업데이트 시간

    private void setupButtonListeners() {
        startBtn.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                Toast.makeText(this, "동작 시작", Toast.LENGTH_SHORT).show();

                if (startTime == 0) {  // 처음 시작할 때만 초기화
                    startTime = System.currentTimeMillis();  // 시작 시간 기록
                }

                lastUpdateTime = System.currentTimeMillis();  // 현재 시간으로 데이터 수집 시작 시점 기록
                startDataCollection();  // 데이터 수집 시작
            } else {
                Toast.makeText(this, "이미 동작 중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        stopBtn.setOnClickListener(v -> {
            if (isRunning) {
                isRunning = false;
                Toast.makeText(this, "동작 중지", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "이미 중지 상태입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        cancelSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SleepUI.super.onBackPressed();
            }
        });
    }

    private void startDataCollection() {
        // 30초 간격으로 데이터를 수집하도록 설정
        new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(30000);  // 30초마다 데이터 업데이트
                    runOnUiThread(() -> {
                        updateGraph();  // 그래프 업데이트
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private float previousValue = 0;

    // 그래프 업데이트
    private void updateGraph() {
        List<Entry> entries = new ArrayList<>();
        int totalPressCount = 0;  // 모든 센서의 뒤척임 횟수 합산

        long currentTime = System.currentTimeMillis(); // 현재 시간 (밀리초 단위)
        long elapsedTime = (currentTime - startTime) / 1000;  // 0초부터 180초까지의 시간 (초 단위)
        if (elapsedTime >= 180) {
            elapsedTime = 180;
            return;
        } // 180초가 넘으면 더 이상 그래프를 업데이트하지 않음
        int numOfEntries = (int) (elapsedTime / 30);  // 30초 간격으로 몇 개의 데이터가 있을지 계산

        // X축 시간: 30초 간격으로 데이터 추가 (0, 30, 60, 90, 120, 150, 180, 210, ...)
        for (int i = 0; i <= numOfEntries; i++) {
            // 누적된 뒤척임 횟수 계산 (각 센서의 값 합산)
            totalPressCount += pressCounts[i];

            // X축 값은 30초 간격으로 증가 (0, 30, 60, ...)
            float xValue = i * 30;  // 0, 30, 60, ...

            entries.add(new Entry(xValue, totalPressCount));  // (시간, 누적된 뒤척임 횟수)
        }

        // 하나의 데이터 세트 생성: 누적된 뒤척임 횟수
        LineDataSet dataSet = new LineDataSet(entries, "뒤척임 횟수");
        dataSet.setColor(Color.BLUE);  // 라인의 색은 파란색으로 설정
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawValues(true);  // 값 텍스트 표시 활성화

        // 그래프에 값 텍스트를 상태로 변환하여 표시
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // 전체 누적 뒤척임 횟수 기준으로 상태를 변경
                if (value >= 5 + previousValue) {
                    previousValue = value;
                    return "LIGHT";  // 누적 횟수가 10 이상이면 LIGHT로 표시
                } else {
                    previousValue = value;
                    return "DEEP";  // 그렇지 않으면 DEEP으로 표시
                }
            }
        });

        // 그래프에 데이터 세트 적용
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // 그래프 새로고침

        // X축 레이블 조정 (실시간으로 동적으로 X축 레이블을 표시)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(numOfEntries, true);
    }
}

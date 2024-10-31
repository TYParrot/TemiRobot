package com.example.dementia;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class alarmSet extends AppCompatActivity {

    private ImageView pillImg;
    private Spinner ampm;
    private Spinner hour;
    private Spinner minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        pillImg = findViewById(R.id.pillImg);
        ampm = findViewById(R.id.ampm);
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);

        //Spinner 목록 불러오기
        initSpinner();
    }

    //Spinner 목록을 string.xml으로부터 불러와서 layout의 spinner에 할당함.
    private void initSpinner(){
        ArrayAdapter<CharSequence> ampmAdapter = ArrayAdapter.createFromResource(this, R.array.ampm_array, android.R.layout.simple_spinner_item);
        ampmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ampm.setAdapter(ampmAdapter);

        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this, R.array.hour_array, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hour.setAdapter(hourAdapter);

        ArrayAdapter<CharSequence> minuteAdapter = ArrayAdapter.createFromResource(this, R.array.minutes_array, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minute.setAdapter(minuteAdapter);
    }
}
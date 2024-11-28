package com.example.dementia.Function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dementia.R;

import java.util.ArrayList;
import java.util.List;

// AlarmAdapter와 AlarmViewHolder를 구현
public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder> {

    private List<AlarmListDataSet> alarmListData;

    // 생성자: 데이터 리스트를 초기화
    public AlarmListAdapter(List<AlarmListDataSet> alarmListData) {
        if (alarmListData == null) {
            System.out.println("현재 알람 데이터가 없습니다.");
            this.alarmListData = new ArrayList<>(); // 비어있는 리스트로 초기화
        } else {
            this.alarmListData = alarmListData;
            // 알람 데이터 출력 (디버깅용)
            for (AlarmListDataSet alarm : alarmListData) {
                System.out.println("Alarm ID: " + alarm.getAlarmID());
                System.out.println("Pill Image URI: " + alarm.getPillImgUri());
                System.out.println("Selected Time: " + alarm.getSelectedTime());
            }
        }
    }

    // 어댑터 데이터 업데이트 메서드 (추가)
    public void updateData(List<AlarmListDataSet> newAlarmListData) {
        if (newAlarmListData == null) {
            this.alarmListData = new ArrayList<>();
        } else {
            this.alarmListData = newAlarmListData;
        }
        notifyDataSetChanged(); // UI 갱신
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_alarm_alarm_item, parent, false);
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        AlarmListDataSet alarm = alarmListData.get(position);
        holder.bind(alarm);
    }

    @Override
    public int getItemCount() {
        return alarmListData != null ? alarmListData.size() : 0;
    }

    // ViewHolder 클래스
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        private ImageView pillImg;
        private TextView alarmTime;
        private TextView[] weekdays;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            // 뷰 초기화
            pillImg = itemView.findViewById(R.id.alarmListImg);
            alarmTime = itemView.findViewById(R.id.selectedTime);
            weekdays = new TextView[]{
                    itemView.findViewById(R.id.mon),
                    itemView.findViewById(R.id.tue),
                    itemView.findViewById(R.id.wed),
                    itemView.findViewById(R.id.thu),
                    itemView.findViewById(R.id.fri),
                    itemView.findViewById(R.id.sat),
                    itemView.findViewById(R.id.sun)
            };
        }

        // 데이터 바인딩 메서드
        public void bind(AlarmListDataSet alarmData) {
            Context context = itemView.getContext();

            // 알림 이미지 URI를 리소스로 변환하여 설정
            String pillImgUri = alarmData.getPillImgUri();
            if (pillImgUri != null) {
                int resourceID = context.getResources().getIdentifier(
                        pillImgUri, "drawable", context.getPackageName());
                if (resourceID != 0) {
                    pillImg.setImageResource(resourceID);
                } else {
                    pillImg.setImageResource(R.drawable.pill); // 기본 이미지 설정
                }
            } else {
                pillImg.setImageResource(R.drawable.pill); // 기본 이미지 설정
            }

            // 선택된 시간 설정
            alarmTime.setText(alarmData.getSelectedTime());

            // 요일 선택 상태를 설정
            boolean[] selectedDays = alarmData.getSelectedDays();
            for (int i = 0; i < weekdays.length; i++) {
                weekdays[i].setVisibility(selectedDays != null && selectedDays[i] ? View.VISIBLE : View.GONE);
            }
        }
    }
}

package com.example.dementia.Function;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dementia.R;


import java.util.List;


//AlarmAdapter와 AlarmViewHolder를 같이 구현하였음.
public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder> {

    private List<AlarmListDataSet> alarmListData;

    public AlarmListAdapter(List<AlarmListDataSet> alarmListData){

        if(alarmListData == null){
            System.out.println("현재 데이터 없음.");
        }else{
            // 각 알람에 대한 정보를 출력
            for (AlarmListDataSet alarm : alarmListData) {
                System.out.println("Alarm ID: " + alarm.getAlarmID());
                System.out.println("Pill Image URI: " + alarm.getPillImgUri());
                System.out.println("Selected Time: " + alarm.getSelectedTime());
            }

        }

        this.alarmListData = alarmListData;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_alarm_alarm_item, parent, false);
        return new AlarmViewHolder(itemView);
    }

    //데이터 바인딩 부분
    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        AlarmListDataSet alarm = alarmListData.get(position);
        holder.bind(alarm);
    }

    @Override
    public int getItemCount() {
        if(alarmListData == null){
            return 0;
        }else {
            return alarmListData.size();
        }
    }

    //아래부터는 AlarmViewHolder
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        private ImageView pillImg;
        private TextView alarmTime;
        private TextView[] weekdays;

        public AlarmViewHolder(View itemView) {

            super(itemView);

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

        public void bind(AlarmListDataSet alarmData) {
            pillImg.setImageURI(alarmData.getPillImgUri());
            alarmTime.setText(alarmData.getSelectedTime());

            boolean[] selectedDays = alarmData.getSelectedDays();

            for (int i = 0; i < 7; i++) {
                weekdays[i].setVisibility(selectedDays[i] ? View.VISIBLE : View.GONE);
            }
        }

    }
}

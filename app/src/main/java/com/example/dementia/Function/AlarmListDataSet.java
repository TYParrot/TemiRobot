package com.example.dementia.Function;

import android.net.Uri;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

//{"name":"Lisa", "age":23}
public class AlarmListDataSet {
    private int alarmID;
    private Uri pillImgUri;
    private String selectedDayString; // static을 제거하고 인스턴스 변수로 변경
    private String selectedTime;

    // 생성자
    public AlarmListDataSet(int alarmID, Uri pillImgUri, boolean[] selectedDays, int hour, int minute) {
        this.alarmID = alarmID;
        this.pillImgUri = pillImgUri;
        this.selectedDayString = booleanToString(selectedDays).toString();
        this.selectedTime = String.format("%02d:%02d", hour, minute);
    }

    // 배열을 문자열로 변환
    private StringBuilder booleanToString(boolean[] selectedDays){
        StringBuilder sb = new StringBuilder();
        String[] dayOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < selectedDays.length; i++) {
            if (selectedDays[i]) {
                sb.append(dayOfWeek[i]);
            }
        }
        return sb;
    }

    // JSON으로 변환
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("alarmID", alarmID);
        jsonObject.put("pillImgUri", pillImgUri.toString());
        jsonObject.put("selectedDays", selectedDayString);
        jsonObject.put("selectedTime", selectedTime);
        return jsonObject;
    }

    // 문자열을 boolean 배열로 변환
    private static boolean[] stringToBooleanArrayStatic(String str) {
        boolean[] result = new boolean[7]; // 7일을 위한 배열
        String[] dayOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < dayOfWeek.length; i++) {
            if (str.contains(dayOfWeek[i])) {
                result[i] = true;
            }
        }
        return result;
    }

    // 문자열을 boolean 배열로 변환
    private boolean[] stringToBooleanArray(String str) {
        boolean[] result = new boolean[7]; // 7일을 위한 배열
        String[] dayOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < dayOfWeek.length; i++) {
            if (str.contains(dayOfWeek[i])) {
                result[i] = true;
            }
        }
        return result;
    }

    // JSON 파일에서 불러오기
    public static ArrayList<AlarmListDataSet> loadFromJSONFile(Context context) {
        ArrayList<AlarmListDataSet> alarmList = new ArrayList<>();
        String jsonString;

        try (InputStream is = context.openFileInput("alarms.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return alarmList; // 빈 리스트 반환
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int alarmID = jsonObject.getInt("alarmID");
                Uri pillImgUri = Uri.parse(jsonObject.getString("pillImgUri"));
                String selectedDaysString = jsonObject.getString("selectedDays");
                boolean[] selectedDays = stringToBooleanArrayStatic(selectedDaysString);
                String[] timeParts = jsonObject.getString("selectedTime").split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);

                alarmList.add(new AlarmListDataSet(alarmID, pillImgUri, selectedDays, hour, minute));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return alarmList;
    }

    // Getter 메소드들
    public int getAlarmID() {
        return alarmID;
    }

    public Uri getPillImgUri() {
        return pillImgUri;
    }

    public boolean[] getSelectedDays() {
        return stringToBooleanArray(selectedDayString);
    }

    public String getSelectedTime() {
        return selectedTime;
    }
}


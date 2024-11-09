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
    //SharedPreferences는 배열 없음.
    private static String selectedDayString;
    private String selectedTime;

    // 생성자
    public AlarmListDataSet(int alarmID, Uri pillImgUri, boolean[] selectedDays, int hour, int minute) {

        this.alarmID = alarmID;
        this.pillImgUri = pillImgUri;
        this.selectedDayString = booleanToString(selectedDays).toString();
        this.selectedTime = String.format("%02d:%02d", hour, minute);
    }

    //배열 저장 불가능하므로, 형태를 바꿔줌.
    private StringBuilder booleanToString(boolean[] selectedDays){
        StringBuilder sb = new StringBuilder();
        String[] dayOfWeek = {"Mon", "Tue","Wed","Thu","Fri","Sat","Sun"};

        for(int i = 0; i<selectedDays.length; i++){
            if(selectedDays[i]){
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

    // JSON 파일 저장
    public static void saveToJSONFile(ArrayList<AlarmListDataSet> alarmList, Context context) {
        JSONArray jsonArray = new JSONArray();
        for (AlarmListDataSet alarm : alarmList) {
            try {
                jsonArray.put(alarm.toJSON());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try (OutputStream os = context.openFileOutput("alarms.json", Context.MODE_PRIVATE)) {
            os.write(jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // JSON 파일 읽기
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
                boolean[] selectedDays = stringToBooleanArray(jsonObject.getString("selectedDays"));
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

    // 문자열을 boolean 배열로 변환하는 메소드
    private static boolean[] stringToBooleanArray(String str) {
        boolean[] result = new boolean[7]; // 7일을 위한 배열
        String[] dayOfWeek = {"Mon", "Tue","Wed","Thu","Fri","Sat","Sun"};
        for (int i = 0; i < dayOfWeek.length; i++) {
            if (selectedDayString.contains(dayOfWeek[i])) {
                result[i] = true;
            }
        }
        return result;
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

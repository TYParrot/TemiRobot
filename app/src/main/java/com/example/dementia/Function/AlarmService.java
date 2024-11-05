package com.example.dementia.Function;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

//백그라운드에서도 돌아가도록 할 예정
public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

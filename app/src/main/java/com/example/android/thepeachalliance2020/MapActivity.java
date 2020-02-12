package com.example.android.thepeachalliance2020;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;;
import com.example.android.thepeachalliance2020._superActivities.DialogMaker;
import com.example.android.thepeachalliance2020.utils.TimerUtil;

public class MapActivity extends DialogMaker {
    public ImageView iv_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        iv_field = findViewById(R.id.imageView);
        TimerUtil.mTimerView = findViewById(R.id.tv_timer);


    }
}

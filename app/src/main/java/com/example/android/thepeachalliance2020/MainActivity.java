package com.example.android.thepeachalliance2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.thepeachalliance2020.Managers.InputManager;

public class MainActivity extends AppCompatActivity {

    public static TextView tv_versionNumber, tv_teamNumber;
    public static Spinner sp_triggerScoutNamePopup, sp_triggerTabletIDPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initViews();
        InputManager.getScoutNames();
        // InputManager.recoverUserData(); //Add this in its where it gets the last used data.
        updateUserData();
    }

    //Set all UI text values
    public static void updateUserData() {
        tv_versionNumber.setText(String.valueOf("Version: " + InputManager.mAppVersion));
        tv_teamNumber.setText(String.valueOf(InputManager.mTeamNum));

    }


    public void initViews() {
        tv_versionNumber = findViewById(R.id.tv_versionNumber);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);

    }

}
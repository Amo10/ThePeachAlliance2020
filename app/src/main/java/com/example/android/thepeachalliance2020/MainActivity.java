package com.example.android.thepeachalliance2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.thepeachalliance2020.Managers.InputManager;

public class MainActivity extends AppCompatActivity {

    public static TextView version_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initViews();

        // InputManager.recoverUserData(); //Add this in its where it gets the last used data.
        updateUserData();
    }

    //Set all UI text values
    public static void updateUserData() {
        version_number.setText(String.valueOf("Version: " + InputManager.mAppVersion));
    }


    public void initViews() {
        version_number = findViewById(R.id.version_number);
    }

}
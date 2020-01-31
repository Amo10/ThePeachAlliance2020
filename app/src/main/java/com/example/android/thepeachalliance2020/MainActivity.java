package com.example.android.thepeachalliance2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020._superDataClasses.AppCc;
import com.example.android.thepeachalliance2020._superDataClasses.Cst;

public class MainActivity extends AppCompatActivity {

    public static TextView tv_versionNumber, tv_teamNumber;
    public static Spinner sp_triggerScoutNamePopup, sp_triggerTabletIDPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Set Scout ID from stored data
        //InputManager.mScoutId = AppCc.getSp("scoutId", 0);


        InputManager.getScoutNames();
        initViews();
        initPopups();

        //InputManager.recoverUserData();
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


    //Create the backup, scout name, and ID popups
    public void initPopups() {

        //Declare scout name popup
        sp_triggerScoutNamePopup = (Spinner) findViewById(R.id.btn_triggerScoutNamePopup);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, R.layout.main_popup_header_name, Cst.SCOUT_NAMES);

        sp_triggerScoutNamePopup.setAdapter(nameAdapter);

        sp_triggerScoutNamePopup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mScoutName = sp_triggerScoutNamePopup.getSelectedItem().toString();
                //AppCc.setSp("scoutName", InputManager.mScoutName);
                updateUserData();
        }
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

        //Declare Scout ID popup
        sp_triggerTabletIDPopup = (Spinner) findViewById(R.id.btn_triggerTabletIDPopup);
        ArrayAdapter<Integer> idAdapter = new ArrayAdapter<Integer>(this, R.layout.main_popup_header_id, Cst.SCOUT_IDS);

        sp_triggerTabletIDPopup.setAdapter(idAdapter);

        sp_triggerTabletIDPopup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mScoutId = (int) sp_triggerTabletIDPopup.getSelectedItem();
                updateUserData();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

    }

}
package com.example.android.thepeachalliance2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020._superActivities.DialogMaker;
import com.example.android.thepeachalliance2020._superDataClasses.AppCc;
import com.example.android.thepeachalliance2020._superDataClasses.Cst;
import com.example.android.thepeachalliance2020.utils.AppUtils;

public class MainActivity extends DialogMaker {

    public static EditText et_matchNum;
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
        initListeners();

        //InputManager.recoverUserData();
        updateUserData();


    }

    //Set all UI text values
    public static void updateUserData() {
        tv_versionNumber.setText(String.valueOf("Version: " + InputManager.mAppVersion));
        tv_teamNumber.setText(String.valueOf(InputManager.mTeamNum));
        et_matchNum.setText(String.valueOf(InputManager.mMatchNum));


    }


    public void initViews() {
        tv_versionNumber = findViewById(R.id.tv_versionNumber);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        et_matchNum = findViewById(R.id.et_matchNum);


    }
    //Add listeners to map and matchNum editor.
    public void initListeners() {
        et_matchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing, necessary for TextChangedListeners.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing, necessary for TextChangedListeners.
            }

            //Updates match number after altered
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    return;
                }

                int matchNum = AppUtils.StringToInt(editable.toString());

                InputManager.mMatchNum = matchNum;
                InputManager.updateTeamNum();
                tv_teamNumber.setText(String.valueOf(InputManager.mTeamNum));
            }
        });
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
        ArrayAdapter<String> idAdapter = new ArrayAdapter<String>(this, R.layout.main_popup_header_id, Cst.SCOUT_IDS);

        sp_triggerTabletIDPopup.setAdapter(idAdapter);

        sp_triggerTabletIDPopup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mTabletID = sp_triggerTabletIDPopup.getSelectedItem().toString();
                updateUserData();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

    }

    public void onClickStartScouting(View view) {
        if (InputManager.mScoutName.equals("unselected") || InputManager.mMatchNum == 0 || InputManager.mTeamNum == 0) {
            Toast.makeText(getBaseContext(), "There is null information!", Toast.LENGTH_SHORT).show();
        }
        else if (InputManager.mMatchNum > Cst.FINAL_MATCH ) {
            //Doesn't let a Scout scout if they have a match number that doesn't exist
            Toast.makeText(getBaseContext(), "Please Input a Valid Match Number", Toast.LENGTH_SHORT).show();
        }
        else {
            open(PregameActivity.class, null, false, true);
        }
    }

}
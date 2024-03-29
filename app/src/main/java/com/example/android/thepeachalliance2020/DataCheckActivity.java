package com.example.android.thepeachalliance2020;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020._superDataClasses.Cst;
import com.example.android.thepeachalliance2020._superActivities.DialogMaker;
import com.example.android.thepeachalliance2020.utils.AppUtils;
import com.example.android.thepeachalliance2020.utils.QRDisplayActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.valueOf;

public class DataCheckActivity extends DialogMaker {

    EditText et_matchNum;
    EditText et_teamNum;

    Spinner name_spinner;

    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacheck);
        context = this;
        //Declare team and match variables
        et_matchNum = findViewById(R.id.matchET);
        et_teamNum = findViewById(R.id.teamET);

        et_matchNum.setText(valueOf(InputManager.mMatchNum));
        et_teamNum.setText(valueOf(InputManager.mTeamNum));


        //Declare name spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.datacheck_dropdown_name, Cst.SCOUT_NAMES);
        name_spinner = (Spinner) findViewById(R.id.spinner_name);

        name_spinner.setAdapter(spinnerAdapter);
        name_spinner.setSelection(((ArrayAdapter<String>) name_spinner.getAdapter()).getPosition(InputManager.mScoutName));

        name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mScoutName = name_spinner.getSelectedItem().toString();
                InputManager.mScoutid = Integer.valueOf(Cst.scoutids.get(InputManager.mScoutName));

            }

            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });
    }

    //Records match data and saves it as a QR
    public void onClickToQR(View view) {
        //If anything is empty in the activity, it will prevent the user from entering the QR activity. Otherwise, save data and go to QR activity
        if (et_teamNum.getText().toString().equals("") || Integer.valueOf(et_teamNum.getText().toString()) == 0 || et_matchNum.getText().toString().equals("") || Integer.valueOf(et_matchNum.getText().toString()) == 0) {
            Toast.makeText(getBaseContext(), "There is null information!", Toast.LENGTH_SHORT).show();
        } else {

            InputManager.mTeamNum = AppUtils.StringToInt(et_teamNum.getText().toString());
            InputManager.mMatchNum = AppUtils.StringToInt(et_matchNum.getText().toString());
            InputManager.initMatchKey();
            //Records all match data. Put all OneTimeMatchData in mOneTimeMatchData to compress into header.
            try {
                InputManager.mOneTimeMatchData.put("matchInfo", InputManager.matchKey);
                InputManager.mOneTimeMatchData.put("startingLocationX", InputManager.mStartPosX);
                InputManager.mOneTimeMatchData.put("startingLocationY", InputManager.mStartPosY);
                InputManager.mOneTimeMatchData.put("preload", InputManager.mPreload);
                InputManager.mOneTimeMatchData.put("isNoShow", InputManager.isNoShow);
                InputManager.mOneTimeMatchData.put("autoMove", InputManager.mAutoMove);
                InputManager.mOneTimeMatchData.put("teleopTime", InputManager.mTeleopTime);
                InputManager.mOneTimeMatchData.put("fieldOrientation", InputManager.mFieldFlip);
                InputManager.mOneTimeMatchData.put("timeStamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                InputManager.mOneTimeMatchData.put("appVersion", InputManager.mAppVersion);
                //InputManager.mOneTimeMatchData.put("timerStarted", InputManager.mTimerStarted);
                InputManager.mClimbData.put("type", "climb");
                InputManager.mClimbData.put("time", InputManager.climbTime);
                InputManager.mClimbData.put("x", InputManager.climbX);
                InputManager.mClimbData.put("y", InputManager.climbY);
                JSONObject park = new JSONObject();
                park.put("attempted", InputManager.parkAttempt);
                park.put("cSuccess", InputManager.parkActual);
                JSONObject self = new JSONObject();
                self.put("attempted", InputManager.climb1Attempt);
                self.put("cSuccess", InputManager.climb1Actual);
                self.put("level", InputManager.climbLevel);
                JSONObject bot1 = new JSONObject();
                bot1.put("attempted", InputManager.climb1Attempt);
                bot1.put("cSuccess", InputManager.climb1Actual);
                JSONObject bot2 = new JSONObject();
                bot2.put("attempted", InputManager.climb1Attempt);
                bot2.put("cSuccess", InputManager.climb1Actual);
                InputManager.mClimbData.put("self", self);
                InputManager.mClimbData.put("park", park);
                InputManager.mClimbData.put("bot1", bot1);
                InputManager.mClimbData.put("bot2", bot2);
                InputManager.mRealTimeMatchData.put(InputManager.mClimbData);
                InputManager.mColorData.put("type", "color");
                InputManager.mColorData.put("position", InputManager.colorPosition);
                InputManager.mColorData.put("rotation", InputManager.colorRotate);
                InputManager.mRealTimeMatchData.put(InputManager.mColorData);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            InputManager.mScoutNameSave = InputManager.mScoutName;
            InputManager.mTabletIDSave = InputManager.mTabletID;
            InputManager.mFieldFlipSave = InputManager.mFieldFlip;
            open(QRDisplayActivity.class, null, false, false);
        }
    }

    //If android back button is pressed, warns the user that they will lose information.
    public void onBackPressed() {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("GOING BACK WILL CAUSE LOSS OF DATA")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
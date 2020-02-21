package com.example.android.thepeachalliance2020;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;;
import com.example.android.thepeachalliance2020._superActivities.DialogMaker;
import com.example.android.thepeachalliance2020.utils.AutoDialog;
import com.example.android.thepeachalliance2020.utils.TimerUtil;
import com.example.android.thepeachalliance2020.utils.PregameDialog;
import com.example.android.thepeachalliance2020.Managers.InputManager;

import static com.example.android.thepeachalliance2020.Managers.InputManager.mPreload;
import static com.example.android.thepeachalliance2020.Managers.InputManager.mRealTimeMatchData;
import static com.example.android.thepeachalliance2020.Managers.InputManager.mStartPos;
import static com.example.android.thepeachalliance2020.Managers.InputManager.mTabletType;

import static com.example.android.thepeachalliance2020.utils.AutoDialog.tb_auto_move;
import static com.example.android.thepeachalliance2020.utils.AutoDialog.btn_startTimer;
import static com.example.android.thepeachalliance2020.utils.AutoDialog.btn_teleop;

import static com.example.android.thepeachalliance2020.utils.PregameDialog.btn_to_auto;
import static com.example.android.thepeachalliance2020.utils.PregameDialog.tb_noshow;
import static com.example.android.thepeachalliance2020.utils.PregameDialog.r_preload;
import static com.example.android.thepeachalliance2020.utils.PregameDialog.r_load0;
import static com.example.android.thepeachalliance2020.utils.PregameDialog.r_load1;
import static com.example.android.thepeachalliance2020.utils.PregameDialog.r_load2;
import static com.example.android.thepeachalliance2020.utils.PregameDialog.r_load3;

import static java.lang.String.valueOf;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends DialogMaker {

    final Activity activity = this;

    public LayoutInflater layoutInflater;

    public boolean modeIsIntake = true;
    public static boolean startTimer = true;
    public boolean tele = false;
    public static boolean timerCheck = false;
    public boolean pw = true;
    public boolean selectDialogOpen = false;
    public boolean shotDialogOpen = false;
    public boolean climbDialogOpen = false;
    public boolean isPregame = true;
    public boolean pregamePlace = false;

    public boolean isPopupOpen = false;

    public String element = "";

    public Float time;

    public int actionCount;
    public boolean wasDefended;
    public boolean didUndoOnce = true;

    public String shotType = "";
    public int x;
    public int y;

    public ConstraintLayout selectDialogLayout;
    public ConstraintLayout shotDialogLayout;
    public ConstraintLayout climbDialogLayout;

    public static Button btn_drop;
    public static Button btn_foul;
    public static Button btn_cyclesDefended;
    public static Button btn_undo;
    public static Button btn_climb;

    public static ToggleButton tb_incap;
    public static ToggleButton tb_defense;
    public static ToggleButton tb_wasDefended;

    public static RadioButton made0;
    public static RadioButton made1;
    public static RadioButton made2;
    public static RadioButton made3;
    public static RadioButton made4;
    public static RadioButton made5;
    public static RadioButton made6;
    public static RadioButton made7;
    public static RadioButton miss0;
    public static RadioButton miss1;
    public static RadioButton miss2;
    public static RadioButton miss3;
    public static RadioButton miss4;
    public static RadioButton miss5;
    public static RadioButton miss6;
    public static RadioButton miss7;
    public static RadioGroup radioMiss;
    public static RadioGroup radioMade;



    public static int shotSuccess;
    public static int shotFail;

    public TextView tv_team;
    public Button btn_arrow;
    public ImageView iv_field;

    public JSONObject compressionDic;

    public Fragment fragmentp;
    public FragmentTransaction transactionp;
    public FragmentManager fmp;
    public Fragment fragmenta;
    public FragmentTransaction transactiona;
    public FragmentManager fma;

    public PopupWindow popup = new PopupWindow();
    public PopupWindow popup_fail_success = new PopupWindow();
    public PopupWindow popup_drop_defense = new PopupWindow();

    public RelativeLayout overallLayout;

    //Shot Popup Window
    public TextView tv_shotTitle;

    public ImageView iv_game_element;


    public Handler handler = new Handler();
    //Alert dialog pop up when 10 seconds into teleop
    public Runnable runnable = new Runnable() {
        public void run() {
            tv_team.setVisibility(View.INVISIBLE);
            btn_arrow.setEnabled(true);
            btn_arrow.setVisibility(View.VISIBLE);
        }
    };
    public Handler teleWarningHandler = new Handler();
    public Runnable teleWarningRunnable = new Runnable() {
        public void run() {
            if (!tele) {
                new AlertDialog.Builder(activity)
                        .setTitle("YOU ARE 10 SECONDS INTO TELEOP!!")
                        .setPositiveButton("GO TO TELEOP", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                popup.dismiss();
                                popup_fail_success.dismiss();
                                if (shotDialogOpen) {
                                    //placementDialog.dismiss();
                                }
                                pw = true;
                                toTeleop();
                            }
                        })
                        .setNegativeButton("STAY IN AUTO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    };

    public List<Object> actionList;
    public Map<Integer, List<Object>> actionDic;

    public Dialog placementDialog;
    public Dialog shotDialog;
    public Dialog climbDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        iv_field = findViewById(R.id.imageView);
        btn_undo = findViewById(R.id.btn_undo);
        tb_incap = findViewById(R.id.tbtn_incap);
        tb_defense = findViewById(R.id.tbtn_defense);
        btn_foul = findViewById(R.id.btn_foul);
        btn_climb = findViewById(R.id.btn_climb);
        btn_arrow = findViewById(R.id.btn_next);
        tv_team = findViewById(R.id.tv_teamNum);
        btn_drop = findViewById(R.id.btn_dropped);

        btn_cyclesDefended = findViewById(R.id.btn_cyclesDefended);

        TimerUtil.mTimerView = findViewById(R.id.tv_timer);


        fragmentp = new PregameDialog();
        fmp = getSupportFragmentManager();
        transactionp = fmp.beginTransaction();

        overallLayout = findViewById(R.id.field);

        layoutInflater = (LayoutInflater) MapActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        //Set how big popups are for each tablet type
        if (mTabletType.equals("fire")) {
            popup = new PopupWindow((ConstraintLayout) layoutInflater.inflate(R.layout.map_popup_select, null), 620, 450, false);
        }
        popup.setOutsideTouchable(false);
        popup.setFocusable(false);

        btn_climb.setEnabled(false);
        tb_defense.setEnabled(false);
        tb_incap.setEnabled(false);
        btn_foul.setEnabled(false);
        btn_undo.setEnabled(false);

        if (InputManager.mAllianceColor.equals("red")) {
            transactionp.add(R.id.left_menu, fragmentp, "FRAGMENTPREGAME");
        } else if (InputManager.mAllianceColor.equals("blue")) {
            transactionp.add(R.id.right_menu, fragmentp, "FRAGMENTPREGAME");
        }
        transactionp.commit();

        if (TimerUtil.matchTimer != null) {
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            timerCheck = false;
            startTimer = true;
            pw = false;
        }
        InputManager.isNoShow = true;
        tv_team.setText(valueOf(InputManager.mTeamNum));

        mRealTimeMatchData = new JSONArray();
        InputManager.mOneTimeMatchData = new JSONObject();
        InputManager.numFoul = 0;
        InputManager.cyclesDefended = 0;
        isPregame = true;

        btn_drop.setEnabled(false);
        btn_undo.setEnabled(false);
        addTouchListener();

        actionList = new ArrayList<Object>();
        actionDic = new HashMap<Integer, List<Object>>();


        iv_game_element = new ImageView(getApplicationContext());

        //Deincrement Fouls counter upon long click
        btn_foul.setOnLongClickListener((new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (InputManager.numFoul > 0) {
                    int index = -1;
                    for (int i = 0; i < mRealTimeMatchData.length(); i++) {
                        try {
                            String test = mRealTimeMatchData.getString(i);
                            if (test.contains("foul")) {
                                index = i;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mRealTimeMatchData.remove(index);
                    InputManager.numFoul--;
                    btn_foul.setText("PIN FOUL - " + InputManager.numFoul);
                }
                return true;
            }
        }));

        //Deincrement Cycles Defended counter upon long click.
        btn_cyclesDefended.setOnLongClickListener((new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (InputManager.cyclesDefended > 0) {
                    int index = -1;
                    //Increment through mRealTimeMatchData to identify the last cyclesDefended to remove.
                    for (int i = 0; i < mRealTimeMatchData.length(); i++) {
                        try {
                            String test = mRealTimeMatchData.getString(i);
                            if (test.contains("cyclesDefended")) {
                                index = i;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mRealTimeMatchData.remove(index);
                    InputManager.cyclesDefended--;
                    btn_cyclesDefended.setText("FAILED SHOTS/DROPS CAUSED - " + InputManager.cyclesDefended);
                }
                return true;
            }
        }));
    }

    public void toAuto() {
        if (!pregamePlace || (r_preload.getCheckedRadioButtonId() == -1))  {
            Toast.makeText(getBaseContext(), "Make Sure You Filled Out All Of The Information!",
                    Toast.LENGTH_SHORT).show();
        } else {
            InputManager.isNoShow = false;
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENTPREGAME");
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();

            fragmenta = new AutoDialog();
            fma = getSupportFragmentManager();
            transactiona = fma.beginTransaction();


            if (InputManager.mAllianceColor.equals("red")) {
                transactiona.add(R.id.left_menu, fragmenta, "FRAGMENTAUTO");
            } else if (InputManager.mAllianceColor.equals("blue")) {
                transactiona.add(R.id.right_menu, fragmenta, "FRAGMENTAUTO");
            }
            transactiona.commit();

            if (TimerUtil.matchTimer != null) {
                TimerUtil.matchTimer.cancel();
                TimerUtil.matchTimer = null;
                TimerUtil.timestamp = 0f;
                TimerUtil.mTimerView.setText("15");
                startTimer = true;
            }
            isPregame = false;

            // add values selected
            mStartPos = "[" + x + "," + y + "]";
            if (r_load0.isChecked()) {
                mPreload = 0;
            } else if (r_load1.isChecked()) {
                mPreload = 1;
            } else if (r_load2.isChecked()) {
                mPreload = 2;
            } else if (r_load3.isChecked()) {
                mPreload = 3;
            }
        }
    }

    public void toTeleop() {
        if (!startTimer) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENTAUTO");
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        dismissPopups();
        //Allows scout to touch screen if a popup used to be open
        if (isPopupOpen && !tb_incap.isChecked()) {
            pw = true;
        }
        tele = true;

        btn_undo.setEnabled(false);
        tb_auto_move.setEnabled(false);


        //If the timer is on and incap isn't checked, make buttons clickable

        if (timerCheck && !tb_incap.isChecked()) {
            btn_climb.setEnabled(true);
            tb_defense.setEnabled(true);
        }

        //mapChange();
        InputManager.mAutoMove = tb_auto_move.isChecked();
    }

    public void onClickAuto(View view) {
        toAuto();
    }
    public void onClickTeleop(View view) {
        toTeleop();
    }
    //Method to dismiss all popups
    public void dismissPopups() {
        popup.dismiss();
        popup_fail_success.dismiss();
        popup_drop_defense.dismiss();
    }

    //When start timer is clicked, adjust timer based on being clicked on and off
    public void onClickStartTimer(View v) {
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);

        teleWarningHandler.removeCallbacks(teleWarningRunnable);
        teleWarningHandler.removeCallbacksAndMessages(null);

        TimerUtil.MatchTimerThread timerUtil = new TimerUtil.MatchTimerThread();

        if (startTimer) {
            pw = true;
            handler.postDelayed(runnable, 15000); //Should be 150000
            teleWarningHandler.postDelayed(teleWarningRunnable, 5000); //Should be 25000
            timerUtil.initTimer();
            btn_startTimer.setText("RESET TIMER");
            timerCheck = true;
            startTimer = false;
            btn_undo.setEnabled(false);
            btn_teleop.setEnabled(true);
            tb_auto_move.setEnabled(true);
            tb_incap.setEnabled(true);
            btn_foul.setEnabled(true);
            InputManager.mTimerStarted = (int) (System.currentTimeMillis() / 1000);
            /*
            InputManager.mTimerStarted = (int) (System.currentTimeMillis() / 1000);
            if (InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.map_storm_reset_red_selector);
            } else if (InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.pregame_storm_reset_blue_selector);
            }
            */

        } else if (!startTimer) {
            pw = false;
            InputManager.numFoul = 0;
            tb_incap.setEnabled(false);
            tb_incap.setChecked(false);
            btn_foul.setEnabled(false);
            tb_auto_move.setEnabled(false);
            tb_auto_move.setChecked(false);
            //btn_climb.setEnabled(false);
            tb_auto_move.setEnabled(false);
            tb_auto_move.setChecked(false);
            btn_teleop.setEnabled(false);
            btn_undo.setEnabled(false);
            btn_drop.setEnabled(false);
            //actionDic.clear();
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            btn_startTimer.setText("START TIMER");
            //overallLayout.removeView(iv_game_element);
            dismissPopups();
            startTimer = true;
            timerCheck = false;
            isPopupOpen = false;
            //preload();
            InputManager.numFoul = 0;
            actionCount = 0;
            btn_foul.setText("PIN FOUL - " + InputManager.numFoul);
            mRealTimeMatchData = new JSONArray();

            // Make preload enabled when you reset the timer.
            /*            //preloadEnabled(true);

            if (InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.map_storm_btn_red_selector);
            } else if (InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.map_storm_btn_blue_selector);
            }
            mapChange();
             */
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener() {
        overallLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (pw && timerCheck) {
                        x = (int) motionEvent.getX();
                        y = (int) motionEvent.getY();
                        Log.e("Xcoordinate", String.valueOf(x));
                        Log.e("Ycoordinate", String.valueOf(y));

                        //Set coordinates of map based on tablet type
                            if (!(x > 1130 || y > 580)) {
                                pw = true;
                                time = TimerUtil.timestamp;
                                initSelect();
                                //initPlacement();
                                modeIsIntake = false;
                                selectDialogOpen = true;
                            }
                    } else if (isPregame && !tb_noshow.isChecked()) {
                        x = (int) motionEvent.getX();
                        y = (int) motionEvent.getY();
                        Log.e("Xcoordinate", String.valueOf(x));
                        Log.e("Ycoordinate", String.valueOf(y));

                        //Set coordinates of map based on tablet type
                        if (!(x > 1130 || y > 580 || x < 225) && InputManager.mAllianceColor.equals("red")) {
                            placePregame();
                        } else if (!(x > 915 || y > 580) && InputManager.mAllianceColor.equals("blue")) {
                            placePregame();
                        }
                    }
                }
                return false;
            }
        });
    }

    public void onClickIncap(View v) {

        //If incap is checked, disable buttons
        if (tb_incap.isChecked()) {
            dismissPopups();

            btn_climb.setEnabled(false);
            btn_drop.setEnabled(false);
            btn_foul.setEnabled(false);
            tb_defense.setEnabled(false);

            if (!tele) {
                tb_auto_move.setEnabled(false);
            }

            pw = false;

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "incap");
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

            undoDicAdder("NA", "NA", "incap");

        }

        //If incap isn't checked, re-enable buttons
        else if (!tb_incap.isChecked()) {
            undoDicAdder("NA", "NA", "unincap");
            pw = true;

            btn_foul.setEnabled(true);

            if (!tele) {
                tb_auto_move.setEnabled(true);
            } else {
                if (!tb_defense.isChecked()) {
                    btn_climb.setEnabled(true);
                }
                tb_defense.setEnabled(true);
            }

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "unincap");
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

        }
        //mapChange();
        Log.i("mRealTimeMatchDataVal", mRealTimeMatchData.toString());
    }

    public void onClickUndo(View v) {
        dismissPopups();
        pw = true;
        int index = -1;
        for (int i = 0; i < mRealTimeMatchData.length(); i++) {
            try {
                String hf = mRealTimeMatchData.getString(i);
                if (hf.contains("intake") || hf.contains("high") || hf.contains("low") || hf.contains("drop") || hf.contains("incap") || hf.contains("unincap") || hf.contains("startDefense") || hf.contains("endDefense")) {
                    index = i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e("indexVals", String.valueOf(index));
        Log.e("mRealTimeMatchDataVals", mRealTimeMatchData.toString());

        //Remove previous object from mRealTimeMatchData
        mRealTimeMatchData.remove(index);
        if (actionCount > 0) {
            actionCount = actionCount - 1;

            if (actionDic.get(actionCount).get(3).equals("high")) {
                overallLayout.removeView(iv_game_element);
            } else if (actionDic.get(actionCount).get(3).equals("low")) {
                overallLayout.removeView(iv_game_element);
            } else if (actionDic.get(actionCount).get(3).equals("drop")) {
                btn_drop.setEnabled(true);
                modeIsIntake = false;
                //mode = "placement";
            } else if (actionDic.get(actionCount).get(3).equals("incap")) {
                pw = true;
                if (!tele) {
                    tb_defense.setEnabled(false);
                    tb_auto_move.setEnabled(true);
                    btn_climb.setEnabled(false);
                } else if (tele) {
                    tb_defense.setEnabled(true);
                    tb_defense.bringToFront();
                    if (!tb_defense.isChecked()) {
                        btn_foul.setEnabled(true);
                        btn_climb.setEnabled(true);
                    }
                }
                tb_incap.setChecked(false);
            } else if (actionDic.get(actionCount).get(3).equals("unincap")) {
                btn_climb.setEnabled(false);
                btn_drop.setEnabled(false);
                btn_foul.setEnabled(false);
                tb_defense.setEnabled(false);
                tb_incap.setChecked(true);

                pw = false;

            } else if (actionDic.get(actionCount).get(3).equals("defense")) {
                btn_climb.setEnabled(true);
                pw = true;
                tb_defense.setChecked(false);

                //Remove Cycles Defended tracker from UI.
                btn_cyclesDefended.setEnabled(false);
                btn_cyclesDefended.setVisibility(View.INVISIBLE);

            } else if (actionDic.get(actionCount).get(3).equals("undefense")) {
                dismissPopups();
                pw = true;
                btn_climb.setEnabled(false);
                tb_defense.setChecked(true);

                //Show Cycles Defended tracker in UI and keep previous Cycles Defended.
                btn_cyclesDefended.setEnabled(true);
                btn_cyclesDefended.setVisibility(View.VISIBLE);
                btn_cyclesDefended.bringToFront();
            }
            actionDic.remove(actionCount);
            //mapChange();
        } else if (actionCount == 0) {
            actionDic.remove(actionCount);
            //preload();
        }
        btn_undo.setEnabled(false);
        didUndoOnce = true;
    }

    //Method that changes intake status, game mode, and the previous game element
    public void undoGeneric(Boolean btndrop, Boolean intakeVal, String modeGeneric) {

        modeIsIntake = intakeVal;
        overallLayout.removeView(iv_game_element);
    }

    public void initSelect() {
        placementDialog = new Dialog(this);
        placementDialog.setCanceledOnTouchOutside(false);
        placementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDialogLayout = (ConstraintLayout) this.getLayoutInflater().inflate(R.layout.map_popup_select, null);
        placementDialog.setContentView(selectDialogLayout);
        placementDialog.show();
    }

    public void placePregame() {
        initOnlyShape();
        pregamePlace = true;
    }

    public void onClickCancelSelect(View view) {
        pw = true;
        selectDialogOpen = false;
        placementDialog.dismiss();
    }

    public void onClickHigh(View view) {
        shotType = "high";
        selectDialogOpen = false;
        placementDialog.dismiss();
        initShot();
    }

    public void onClickLow(View view) {
        shotType = "low";
        selectDialogOpen = false;
        placementDialog.dismiss();
        initShot();
    }

    public void initShot(){
        shotDialogOpen = true;
        shotDialog = new Dialog(this);
        shotDialog.setCanceledOnTouchOutside(false);
        shotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shotDialogLayout = (ConstraintLayout) this.getLayoutInflater().inflate(R.layout.map_popup_shot, null);
        tv_shotTitle = shotDialogLayout.findViewById(R.id.tv_shotTitle);
        tb_wasDefended = shotDialogLayout.findViewById(R.id.tb_defended);
        miss0 = shotDialogLayout.findViewById(R.id.miss0);
        miss1 = shotDialogLayout.findViewById(R.id.miss1);
        miss2 = shotDialogLayout.findViewById(R.id.miss2);
        miss3 = shotDialogLayout.findViewById(R.id.miss3);
        miss4 = shotDialogLayout.findViewById(R.id.miss4);
        miss5 = shotDialogLayout.findViewById(R.id.miss5);
        miss6 = shotDialogLayout.findViewById(R.id.miss6);
        miss7 = shotDialogLayout.findViewById(R.id.miss7);
        made0 = shotDialogLayout.findViewById(R.id.made0);
        made1 = shotDialogLayout.findViewById(R.id.made1);
        made2 = shotDialogLayout.findViewById(R.id.made2);
        made3 = shotDialogLayout.findViewById(R.id.made3);
        made4 = shotDialogLayout.findViewById(R.id.made4);
        made5 = shotDialogLayout.findViewById(R.id.made5);
        made6 = shotDialogLayout.findViewById(R.id.made6);
        made7 = shotDialogLayout.findViewById(R.id.made7);
        radioMade = shotDialogLayout.findViewById(R.id.radioMade);
        radioMiss = shotDialogLayout.findViewById(R.id.radioMiss);
        tv_shotTitle.setText(shotType + " Shot");
        shotDialog.setContentView(shotDialogLayout);
        shotDialog.show();
    }

    public void recordPlacement() {
        wasDefended = tb_wasDefended.isChecked();
        compressionDic = new JSONObject();
        try {
            compressionDic.put("type", shotType);
            timestamp(time);
            compressionDic.put("x", x);
            compressionDic.put("y", y);
            compressionDic.put("success", shotSuccess);
            compressionDic.put("fail", shotFail);
            compressionDic.put("defended", wasDefended);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRealTimeMatchData.put(compressionDic);

        Log.i("mRealTimeMatchDataVals?", mRealTimeMatchData.toString());
    }

        public void onClickClimb(View view){
        climbDialogOpen = true;
        initClimb();
    }

    public void initClimb(){
        climbDialogOpen = true;
        climbDialog = new Dialog(this);
        climbDialog.setCanceledOnTouchOutside(false);
        climbDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        climbDialogLayout = (ConstraintLayout) this.getLayoutInflater().inflate(R.layout.map_popup_climb, null);
        climbDialog.setContentView(climbDialogLayout);
        climbDialog.show();
    }

    public void onClickCancelShot(View view) {
        pw = true;
        shotDialogOpen = false;
        shotDialog.dismiss();
    }

    public void onClickDrop(View view) {
        selectDialogOpen = false;
        placementDialog.dismiss();
        initOnlyShape();
        undoDicAdder(x, y, "drop");
        compressionDic = new JSONObject();
        try {
            compressionDic.put("type", "drop");
            timestamp(time);
            compressionDic.put("x", x);
            compressionDic.put("y", y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRealTimeMatchData.put(compressionDic);
        Log.i("mRealTimeMatchDataVals?", mRealTimeMatchData.toString());
    }

    public void onClickPickup(View view) {
        selectDialogOpen = false;
        placementDialog.dismiss();
        initOnlyShape();
        undoDicAdder(x, y, "intake");
        compressionDic = new JSONObject();
        try {
            compressionDic.put("type", "intake");
            timestamp(time);
            compressionDic.put("x", x);
            compressionDic.put("y", y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRealTimeMatchData.put(compressionDic);
        Log.i("mRealTimeMatchDataVals?", mRealTimeMatchData.toString());

    }


    public void onClickDone(View view) {
        if((radioMiss.getCheckedRadioButtonId() == -1) || (radioMade.getCheckedRadioButtonId() == -1)) {
            Toast.makeText(getBaseContext(), "Make Sure You Filled Out All Of The Information!",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (miss0.isChecked()) {
                shotFail = 0;
            } else if (miss1.isChecked()) {
                shotFail = 1;
            } else if (miss2.isChecked()) {
                shotFail = 2;
            } else if (miss3.isChecked()) {
                shotFail = 3;
            } else if (miss4.isChecked()) {
                shotFail = 4;
            } else if (miss5.isChecked()) {
                shotFail = 5;
            } else if (miss6.isChecked()) {
                shotFail = 6;
            } else if (miss7.isChecked()) {
                shotFail = 7;
            }

            if (made0.isChecked()) {
                shotSuccess = 0;
            } else if (made1.isChecked()) {
                shotSuccess = 1;
            } else if (made2.isChecked()) {
                shotSuccess = 2;
            }else if (made3.isChecked()) {
                shotSuccess = 3;
            }else if (made4.isChecked()) {
                shotSuccess = 4;
            }else if (made5.isChecked()) {
                shotSuccess = 5;
            }else if (made6.isChecked()) {
                shotSuccess = 6;
            }else if (made7.isChecked()) {
                shotSuccess = 7;
            }
            initShape();
            recordPlacement();
            pw = true;
            shotDialogOpen = false;
            shotDialog.dismiss();
        }
    }

    public void initShape() {
        pw = true;
        overallLayout.removeView(iv_game_element);

        iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
        undoDicAdder(x, y, shotType);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                100,
                100);

        lp.setMargins(x - 50, y - 50, 0, 0);
        iv_game_element.setLayoutParams(lp);
        ((ViewGroup) overallLayout).addView(iv_game_element);

        //mapChange();
    }

    public void initOnlyShape() {
        pw = true;
        overallLayout.removeView(iv_game_element);

        iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                100,
                100);

        lp.setMargins(x - 50, y - 50, 0, 0);
        iv_game_element.setLayoutParams(lp);
        ((ViewGroup) overallLayout).addView(iv_game_element);

        //mapChange();
    }



    //Set map drawable based user mode
    /*
    public void mapChange() {
        if (element.equals("cargo") && !tb_incap.isChecked() && !tb_defense.isChecked()) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
            if (mode.equals("placement")) {
                iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));

                if (field_orientation.contains("left")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_cargo_left);
                } else if (field_orientation.contains("right")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_cargo_right);
                }
            }
        } else if (element.equals("panel") && !tb_incap.isChecked() && !tb_defense.isChecked()) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_panel));
            if (mode.equals("placement")) {
                if (field_orientation.contains("left")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_panel_left);
                } else if (field_orientation.contains("right")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_panel_right);
                }
            }
        }
        if (mode.equals("intake") && !tb_incap.isChecked() && !tb_defense.isChecked()) {
            btn_drop.setEnabled(false);
            if (field_orientation.equals("blue_left")) {
                iv_field.setImageResource(R.drawable.map_field_intake_blue_left);
            } else if (field_orientation.equals("blue_right")) {
                iv_field.setImageResource(R.drawable.map_field_intake_blue_right);
            } else if (field_orientation.equals("red_left")) {
                iv_field.setImageResource(R.drawable.map_field_intake_red_left);
            } else if (field_orientation.equals("red_right")) {
                iv_field.setImageResource(R.drawable.map_field_intake_red_right);
            }
        }
        if (tb_incap.isChecked()) {
            if (field_orientation.contains("right")) {
                iv_field.setImageResource(R.drawable.map_field_incap_right);
            } else if (field_orientation.contains("left")) {
                iv_field.setImageResource(R.drawable.map_field_incap_left);
            }
        } else if (tb_defense.isChecked()) {
            if (mode.equals("intake")) {
                if (field_orientation.equals("blue_left")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_blue_left);
                } else if (field_orientation.equals("blue_right")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_blue_right);
                } else if (field_orientation.equals("red_left")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_red_left);
                } else if (field_orientation.equals("red_right")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_red_right);
                }
            }
            if (element.equals("cargo")) {
                iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
                if (mode.equals("placement")) {
                    iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
                    if (field_orientation.contains("left")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_cargo_left);
                    } else if (field_orientation.contains("right")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_cargo_right);
                    }
                }
            }
            if (element.equals("panel")) {
                iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_panel));
                if (mode.equals("placement")) {
                    if (field_orientation.contains("left")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_panel_left);
                    } else if (field_orientation.contains("right")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_panel_right);
                    }
                }
            }
        }
    }

     */

    public void onClickDefense(View v) {
        btn_climb = (Button) findViewById(R.id.btn_climb);
        if (tb_defense.isChecked()) {
            dismissPopups();
            pw = false;
            btn_climb.setEnabled(false);

            //Show Cycles Defended tracker in UI.
            InputManager.cyclesDefended = 0;
            btn_cyclesDefended.setEnabled(true);
            btn_cyclesDefended.setVisibility(View.VISIBLE);
            btn_cyclesDefended.bringToFront();
            btn_cyclesDefended.setText("FAILED PLACEMENTS/DROPS CAUSED - 0");

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "startDefense");
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

            //mapChange();
            undoDicAdder("NA", "NA", "defense");
        } else if (!tb_defense.isChecked()) {
            btn_climb.setEnabled(true);

            //Remove Cycles Defended tracker from UI.
            btn_cyclesDefended.setEnabled(false);
            btn_cyclesDefended.setVisibility(View.INVISIBLE);

            pw = true;

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "endDefense");
                compressionDic.put("cyclesDefended", InputManager.cyclesDefended);
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

            //mapChange();
            undoDicAdder("NA", "NA", "undefense");
        }
    }

    //Record fouls and make foul counter go up
    public void onClickFoul(View v) throws JSONException {
        InputManager.numFoul++;
        btn_foul.setText("PIN FOUL - " + InputManager.numFoul);
        compressionDic = new JSONObject();
        try {
            compressionDic.put("type", "foul");
            timestamp(TimerUtil.timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRealTimeMatchData.put(compressionDic);
        Log.i("mRealTimeMatchDataVals?", mRealTimeMatchData.toString());
    }

    //Increment when pressed.
    public void onClickCyclesDefended(View v) {
        InputManager.cyclesDefended++;
        btn_cyclesDefended.setText("FAILED PLACEMENTS/DROPS CAUSED - " + InputManager.cyclesDefended);
    }

    //Add timestamp to objects in mRealTimeMatchData
    public void timestamp(Float givenTime) {
        if ((givenTime <= 135 && !tele) || (givenTime > 135 && tele)) {
            try {
                compressionDic.put("time", String.format("%.1f", givenTime) + "*");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                compressionDic.put("time", String.format("%.1f", givenTime));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void undoDicAdder(Object xCoordinate, Object yCoordinate, String type) {
        actionList = new ArrayList<Object>();
        actionList.add(xCoordinate);
        actionList.add(yCoordinate);
        actionList.add("");
        actionList.add(type);
        actionList.add(TimerUtil.timestamp);
        actionDic.put(actionCount, actionList);
        actionCount++;
        didUndoOnce = false;
        btn_undo.setEnabled(true);
    }

    public void onBackPressed() {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("GOING BACK WILL CAUSE LOSS OF DATA")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeCallbacks(runnable);
                        handler.removeCallbacksAndMessages(null);

                        teleWarningHandler.removeCallbacks(teleWarningRunnable);
                        teleWarningHandler.removeCallbacksAndMessages(null);

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

    //Records if a robot showed up
    public void onClickShowUp(View view) {
        //If the show up button is already checked, disables map based buttons
        if (tb_noshow.isChecked()) {
            tv_team.setVisibility(View.INVISIBLE);
            btn_arrow.setEnabled(true);
            btn_arrow.setVisibility(View.VISIBLE);
            btn_to_auto.setEnabled(false);
            r_load0.setEnabled(false);
            r_load1.setEnabled(false);
            r_load2.setEnabled(false);
            r_load3.setEnabled(false);
        } else {
            tv_team.setVisibility(View.VISIBLE);
            btn_arrow.setEnabled(false);
            btn_arrow.setVisibility(View.INVISIBLE);
            btn_to_auto.setEnabled(true);
            r_load0.setEnabled(true);
            r_load1.setEnabled(true);
            r_load2.setEnabled(true);
            r_load3.setEnabled(true);
        }

    }

    //Move to next activity and saves defense data
    public void onClickDataCheck(View v) {
        if (tb_defense.isChecked()) {
            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "endDefense");
                compressionDic.put("cyclesDefended", InputManager.cyclesDefended);
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);
        }

        open(DataCheckActivity.class, null, false, true);
    }
}

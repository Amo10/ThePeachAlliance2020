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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;;
import com.example.android.thepeachalliance2020._superActivities.DialogMaker;
import com.example.android.thepeachalliance2020.utils.AutoDialog;
import com.example.android.thepeachalliance2020.utils.TimerUtil;
import com.example.android.thepeachalliance2020.utils.PregameDialog;
import com.example.android.thepeachalliance2020.Managers.InputManager;

import static com.example.android.thepeachalliance2020.Managers.InputManager.mRealTimeMatchData;
import static com.example.android.thepeachalliance2020.Managers.InputManager.mTabletType;


import static com.example.android.thepeachalliance2020.utils.AutoDialog.tb_auto_move;
import static com.example.android.thepeachalliance2020.utils.AutoDialog.btn_startTimer;
import static com.example.android.thepeachalliance2020.utils.AutoDialog.teleButton;
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
    public boolean startedWObject = false;
    public boolean climbInputted = false;
    public static boolean timerCheck = false;
    public boolean pw = true;
    public boolean isMapLeft = false;
    public boolean dropClick = false;
    public boolean selectDialogOpen = false;
    public boolean shotDialogOpen = false;
    public boolean isPopupOpen = false;

    public int actionCount;
    public boolean didSucceed;
    public boolean wasDefended;
    public boolean shotOutOfField;
    public boolean didUndoOnce = true;
    public Integer climbAttemptCounter = 0;
    public Integer climbActualCounter = 0;
    public Integer level;
    public Integer undoX;
    public Integer undoY;

    public String shotType = "";
    public int x;
    public int y;

    public ConstraintLayout selectDialogLayout;
    public ConstraintLayout shotDialogLayout;


    public static Button btn_drop;
    public static Button btn_foul;
    public static Button btn_cyclesDefended;
    public static Button btn_undo;
    public static Button btn_climb;

    public static ToggleButton tb_incap;
    public static ToggleButton tb_defense;

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
                        .setNegativeButton("STAY IN SANDSTORM", new DialogInterface.OnClickListener() {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        iv_field = findViewById(R.id.imageView);
        btn_undo = findViewById(R.id.btn_undo);
        tb_incap = findViewById(R.id.tbtn_incap);
        tb_defense = findViewById(R.id.tbtn_defence);
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


        if (InputManager.mAllianceColor.equals("red")) {
            transactionp.add(R.id.left_menu, fragmentp, "FRAGMENTPREGAME");
        } else if (InputManager.mAllianceColor.equals("blue")) {
            transactionp.add(R.id.right_menu, fragmentp, "FRAGMENTPREGAME");
        }
        transactionp.commit();

        tv_team.setText(valueOf(InputManager.mTeamNum));

        mRealTimeMatchData = new JSONArray();
        InputManager.mOneTimeMatchData = new JSONObject();
        InputManager.numFoul = 0;
        InputManager.cyclesDefended = 0;

        btn_drop.setEnabled(false);
        btn_undo.setEnabled(false);
        addTouchListener();

        actionList = new ArrayList<Object>();
        actionDic = new HashMap<Integer, List<Object>>();

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
                    btn_cyclesDefended.setText("FAILED PLACEMENTS/DROPS CAUSED - " + InputManager.cyclesDefended);
                }
                return true;
            }
        }));
    }

    public void toAuto() {
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
            handler.postDelayed(runnable, 150000);
            teleWarningHandler.postDelayed(teleWarningRunnable, 25000);
            timerUtil.initTimer();
            btn_startTimer.setText("RESET TIMER");
            timerCheck = true;
            startTimer = false;
            btn_undo.setEnabled(false);
            teleButton.setEnabled(true);
            tb_auto_move.setEnabled(true);
            tb_incap.setEnabled(true);
            btn_foul.setEnabled(true);
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
            teleButton.setEnabled(false);
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
                                initSelect();
                                //initPlacement();
                                modeIsIntake = false;
                                selectDialogOpen = true;
                            }
                    }
                }
                return false;
            }
        });
    }

    public void initPopup(PopupWindow pw2) {
        if (timerCheck) {
            isPopupOpen = true;
            //Set coordinates and size of popup based on tablet type
            if (mTabletType.equals("fire")) {
                pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, 150, 100);
            }
            pw = false;

        }
    }

    public void initSelect() {
        placementDialog = new Dialog(this);
        placementDialog.setCanceledOnTouchOutside(false);
        placementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDialogLayout = (ConstraintLayout) this.getLayoutInflater().inflate(R.layout.map_popup_select, null);
        placementDialog.setContentView(selectDialogLayout);
        placementDialog.show();
    }

    public void onClickCancelSelect(View view) {
        pw = true;
        selectDialogOpen = false;
        placementDialog.dismiss();
    }

    public void onClickHigh(View view) {
        shotType = "High";
        selectDialogOpen = false;
        placementDialog.dismiss();
        initShot();
    }

    public void onClickLow(View view) {
        shotType = "Low";
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
        tv_shotTitle.setText(shotType + " Shot");
        shotDialog.setContentView(shotDialogLayout);
        shotDialog.show();

    }

    public void onClickCancelShot(View view) {
        pw = true;
        shotDialogOpen = false;
        shotDialog.dismiss();
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
            pw = true;
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
        //actionList.add(mode);
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
}

package com.example.android.thepeachalliance2020;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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


import static com.example.android.thepeachalliance2020.utils.AutoDialog.tb_auto_move;
import static com.example.android.thepeachalliance2020.utils.AutoDialog.btn_startTimer;
import static com.example.android.thepeachalliance2020.utils.AutoDialog.teleButton;




import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;


public class MapActivity extends DialogMaker {

    final Activity activity = this;


    public boolean modeIsIntake = true;
    public static boolean startTimer = true;
    public boolean tele = false;
    public boolean startedWObject = false;
    public boolean climbInputted = false;
    public static boolean timerCheck = false;
    public boolean pw = true;
    public boolean isMapLeft = false;
    public boolean dropClick = false;
    public boolean placementDialogOpen = false;
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

    public RelativeLayout placementDialogLayout;

    public static Button btn_drop;
    public static Button btn_foul;
    public static Button btn_cyclesDefended;
    public static Button btn_undo;
    public static Button btn_climb;

    public static ToggleButton tb_incap;
    public static ToggleButton tb_defense;

    public Button btn_arrow;
    public ImageView iv_field;

    public Fragment fragmentp;
    public FragmentTransaction transactionp;
    public FragmentManager fmp;
    public Fragment fragmenta;
    public FragmentTransaction transactiona;
    public FragmentManager fma;

    public PopupWindow popup = new PopupWindow();
    public PopupWindow popup_fail_success = new PopupWindow();
    public PopupWindow popup_drop_defense = new PopupWindow();

    public Handler handler = new Handler();
    //Alert dialog pop up when 10 seconds into teleop
    public Runnable runnable = new Runnable() {
        public void run() {
            //tv_team.setVisibility(View.INVISIBLE);
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
                                if (placementDialogOpen) {
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
        TimerUtil.mTimerView = findViewById(R.id.tv_timer);


        fragmentp = new PregameDialog();
        fmp = getSupportFragmentManager();
        transactionp = fmp.beginTransaction();


        if (InputManager.mAllianceColor.equals("red")) {
            transactionp.add(R.id.left_menu, fragmentp, "FRAGMENTPREGAME");
        } else if (InputManager.mAllianceColor.equals("blue")) {
            transactionp.add(R.id.right_menu, fragmentp, "FRAGMENTPREGAME");
        }
        transactionp.commit();

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

        btn_drop = findViewById(R.id.btn_dropped);

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
            btn_climb.setEnabled(false);
            btn_climb.setChecked(false);
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


}

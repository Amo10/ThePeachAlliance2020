package com.example.android.thepeachalliance2020.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020.R;

import androidx.fragment.app.Fragment;

public class PregameDialog extends Fragment {

    public static View view;
    public static Button btn_startTimer;
    public static Button btn_to_auto;
    public static ToggleButton tb_noshow;
    public static RadioGroup r_preload;
    public static RadioButton r_load0;
    public static RadioButton r_load1;
    public static RadioButton r_load2;
    public static RadioButton r_load3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Declare all of the buttons in the Sandstorm Fragment
        //InputManager.mSandstormEndPosition = "";
        if(InputManager.mAllianceColor.equals("red")) {
            view = inflater.inflate(R.layout.map_pregame_blue, container, false);
            btn_startTimer = view.findViewById(R.id.btn_timer);
            btn_to_auto =view.findViewById(R.id.btn_to_auto);
            tb_noshow = view.findViewById(R.id.tb_noshow);
            r_preload = view.findViewById(R.id.r_preload);
            r_load0 = view.findViewById(R.id.r_load0);
            r_load1 = view.findViewById(R.id.r_load1);
            r_load2 = view.findViewById(R.id.r_load2);
            r_load3 = view.findViewById(R.id.r_load3);
        }
        else if(InputManager.mAllianceColor.equals("blue")) {
            view = inflater.inflate(R.layout.map_pregame_blue, container, false);
            btn_startTimer = view.findViewById(R.id.btn_timer);
            btn_to_auto =view.findViewById(R.id.btn_to_auto);
            tb_noshow = view.findViewById(R.id.tb_noshow);
            r_preload = view.findViewById(R.id.r_preload);
            r_load0 = view.findViewById(R.id.r_load0);
            r_load1 = view.findViewById(R.id.r_load1);
            r_load2 = view.findViewById(R.id.r_load2);
            r_load3 = view.findViewById(R.id.r_load3);
        }

        return view;
    }
}

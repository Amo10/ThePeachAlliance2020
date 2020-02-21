package com.example.android.thepeachalliance2020.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020.R;

public class AutoDialog extends Fragment {

    public static View view;
    public static Button btn_startTimer;
    public static ToggleButton tb_auto_move;
    public static Button btn_teleop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Declare all of the buttons in the Sandstorm Fragment
        //InputManager.mSandstormEndPosition = "";
        if(InputManager.mAllianceColor.equals("red")) {
            view = inflater.inflate(R.layout.map_auto_red, container, false);
            btn_startTimer = view.findViewById(R.id.btn_timer);
            tb_auto_move = view.findViewById(R.id.tgbtn_move);
            btn_teleop =view.findViewById(R.id.btn_to_auto);
        }
        else if(InputManager.mAllianceColor.equals("blue")) {
            view = inflater.inflate(R.layout.map_auto_blue, container, false);
            btn_startTimer = view.findViewById(R.id.btn_timer);
            tb_auto_move = view.findViewById(R.id.tgbtn_move);
            btn_teleop =view.findViewById(R.id.btn_to_auto);
        }

        return view;
    }
}

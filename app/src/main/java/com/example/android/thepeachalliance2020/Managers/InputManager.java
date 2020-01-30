package com.example.android.thepeachalliance2020.Managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InputManager {
    //Match Data Holders
    //Below holds match data
    public static JSONArray mRealTimeMatchData = new JSONArray();
    //keys that only appear once in data
    public static JSONObject mOneTimeMatchData;
    //Below is finaldata to be inputted to QR
    public static JSONObject mRealTimeInputtedData;

    //Main Inputs
    public static String matchKey = "1746Q1-1";
    public static String mAllianceColor = "";
    public static String mScoutName = "unselected";
    public static String mTabletType = "unselected";

    public static int mScoutId = 0;
    public static int mMatchNum = 0;
    public static int mTeamNum = 0;
    public static int mCycleNum = 0;

    public static String mAppVersion = "1.0";

}

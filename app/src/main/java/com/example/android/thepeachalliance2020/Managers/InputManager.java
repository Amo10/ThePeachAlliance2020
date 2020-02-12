package com.example.android.thepeachalliance2020.Managers;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.example.android.thepeachalliance2020.utils.AppUtils;
import com.example.android.thepeachalliance2020._superDataClasses.Match;
import com.example.android.thepeachalliance2020._superDataClasses.Cst;


public class InputManager {
    //Match Data Holders
    //Below holds match data
    public static JSONArray mRealTimeMatchData = new JSONArray();
    //keys that only appear once in data
    public static JSONObject mOneTimeMatchData;
    //Below is finaldata to be inputted to QR
    public static JSONObject mRealTimeInputtedData;

    //Main Inputs

    public static String matchKey = "1678Q3-13";
    public static String mAllianceColor = "";
    public static String mScoutName = "unselected";
    public static String mTabletID = "";
    public static int mMatchNum = 1;
    public static int mTeamNum = 0;
    public static int numFoul = 0;

    public static Boolean mAutoMove = false;
    public static int cyclesDefended = 0;

    public static String mAppVersion = "1.0";
    public static String mDatabaseURL;

    //Populate Scout List
    public static ArrayList<String> getScoutNames() {
        ArrayList<String> finalNamesList = new ArrayList<String>();

        String filePath = Environment.getExternalStorageDirectory().toString() + "/scout";

        String fileName = "Scouts.txt";

        File f = new File(filePath, fileName);

        Log.i("path", filePath);
        Log.i("doesFileExist", f.exists() + "");

        //Retrieve names from text file in internal storage
        if (f.exists()) {
            try {
                JSONObject names = new JSONObject(AppUtils.retrieveSDCardFile("Scouts.txt"));

                JSONArray namesArray = names.getJSONArray("names");
                ArrayList<String> backupNames = new ArrayList<String>();

                for (int i = 0; i < namesArray.length(); i++) {
                    String finalNames = namesArray.getString(i);
                    finalNamesList.add(finalNames);
                }

                //Alphabetically sort names with Backups at the end
                Collections.sort(finalNamesList, String.CASE_INSENSITIVE_ORDER);

                for (int i = finalNamesList.size() - 1; i >= 0; i--) {
                    if (finalNamesList.get(i).contains("Backup")) {
                        backupNames.add(finalNamesList.get(i));
                        finalNamesList.remove(i);
                    }
                }

                Collections.sort(backupNames, String.CASE_INSENSITIVE_ORDER);

                JSONArray backupArray = new JSONArray(backupNames);

                for (int i = 0; i < backupArray.length(); i++) {
                    String moreNames = backupArray.getString(i);
                    finalNamesList.add(moreNames);
                }

                Log.i("scoutNames", finalNamesList.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (!f.exists()) {
            //Populate finalNamesList with 52 Backups
            for (int i = 1; i <= 52; i++) {
                finalNamesList.add("Backup " + i);
            }
        }

        return finalNamesList;
    }

    public static void updateTeamNum() {
        if(mMatchNum <= Cst.FINAL_MATCH) {
            switch (mTabletID) {
                case "Red 1":
                    mTeamNum = Cst.MATCH_LIST.get(mMatchNum).red1;
                    mAllianceColor = "red";
                    break;
                case "Red 2":
                    mTeamNum = Cst.MATCH_LIST.get(mMatchNum).red2;
                    mAllianceColor = "red";
                    break;
                case "Red 3":
                    mTeamNum = Cst.MATCH_LIST.get(mMatchNum).red3;
                    mAllianceColor = "red";
                    break;
                case "Blue 1":
                    mTeamNum = Cst.MATCH_LIST.get(mMatchNum).blue1;
                    mAllianceColor = "blue";
                    break;
                case "Blue 2":
                    mTeamNum = Cst.MATCH_LIST.get(mMatchNum).blue2;
                    mAllianceColor = "blue";
                    break;
                case "Blue 3":
                    mTeamNum = Cst.MATCH_LIST.get(mMatchNum).blue3;
                    mAllianceColor = "blue";
                    break;
            }
        } else {
            mTeamNum = 0;
        }
    }

    public static ArrayList<Match> getMatchSchedule() {
        ArrayList<String> finalNamesList = new ArrayList<String>();

        String filePath = Environment.getExternalStorageDirectory().toString() + "/scout";

        String fileName = "Scouts.txt";

        File f = new File(filePath, fileName);

        Log.i("path", filePath);
        Log.i("doesFileExist", f.exists() + "");

        ArrayList<Match> matches = new ArrayList<>();

        //Retrieve names from text file in internal storage
        if (f.exists()) {
            try {
                JSONObject data = new JSONObject(AppUtils.retrieveSDCardFile("Scouts.txt"));

                JSONArray matchArray = data.getJSONArray("match_schedule");
                matches.add(new Match());
                for (int i = 0; i < matchArray.length(); i++) {
                    Match currMatch = new Match();
                    currMatch.match = matchArray.getJSONObject(i).getString("Match");
                    currMatch.red1 = matchArray.getJSONObject(i).getInt("Red 1");
                    currMatch.red2 = matchArray.getJSONObject(i).getInt("Red 2");
                    currMatch.red3 = matchArray.getJSONObject(i).getInt("Red 3");
                    currMatch.blue1 = matchArray.getJSONObject(i).getInt("Blue 1");
                    currMatch.blue2 = matchArray.getJSONObject(i).getInt("Blue 2");
                    currMatch.blue3 = matchArray.getJSONObject(i).getInt("Blue 3");
                    matches.add(currMatch);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return matches;
    }

}

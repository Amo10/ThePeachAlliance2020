package com.example.android.thepeachalliance2020._superDataClasses;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020._superDataClasses.Match;

public interface Cst {

    String SHARED_PREF = "scout_sp";

    //Saves scout names into shared preferences
    List<String> SCOUT_NAMES = InputManager.getScoutNames();

    List<Match> MATCH_LIST = InputManager.getMatchSchedule();

    int FINAL_MATCH = MATCH_LIST.size() - 1;

    //Saves scout IDS as 1-18
    List<String> SCOUT_IDS = Arrays.asList("Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3");



    //Stores manual compression keys that aren't separated by commas
    Map<String, String> initialCompressKeys = new HashMap<String, String>() {{
        put("startingLevel", "a");
        put("crossedHabLine", "b");
        put("startingLocation", "c");
        put("preload", "d");
        put("isNoShow", "f");
        put("timerStarted", "g");
        put("currentCycle", "h");
        put("scoutID", "j");
        put("scoutName", "k");
        put("appVersion", "m");
        put("assignmentMode", "n");
        put("assignmentFileTimestamp", "p");
        put("sandstormEndPosition", "G");
    }};
    //Compresses specific keys as letters
    Map<String, String> compressKeys = new HashMap<String, String>() {{


        put("cyclesDefended", "J");        put("type", "r");
        put("time", "s");

        put("piece", "t");
        put("zone", "u");
        put("didSucceed", "v");
        put("wasDefended", "w");
        put("structure", "x");
        put("side", "y");
        put("shotOutOfField", "H");
        put("level", "z");


        put("attempted", "B");
        put("actual", "C");
        put("self", "D");
        put("robot1", "E");
        put("robot2", "F");
    }};

    //Compress possible values as certain letters
    Map<String, String> compressValues = new HashMap<String, String>() {{
        put("true", "T");
        put("false", "F");
        put("left", "A");
        put("mid", "B");
        put("right", "C");
        put("far", "D");
        put("cargo", "E");
        put("panel", "G");
        put("none", "H");
        put("QR", "J");
        put("backup", "K");
        put("override", "L");
        put("intake", "M");
        put("placement", "N");
        put("drop", "P");
        put("foul", "Q");
        put("climb", "R");
        put("incap", "S");
        put("unincap", "U");
        put("startDefense", "t");
        put("endDefense", "u");
        put("zone1Left", "V");
        put("zone1Right", "W");
        put("zone2Left", "X");
        put("zone2Right", "Y");
        put("zone3Left", "Z");
        put("zone3Right", "a");
        put("zone4Left", "b");
        put("zone4Right", "c");
        put("leftLoadingStation", "d");
        put("rightLoadingStation", "e");
        put("leftRocket", "f");
        put("rightRocket", "g");
        put("cargoShip", "h");
        put("near", "s");
    }};
}

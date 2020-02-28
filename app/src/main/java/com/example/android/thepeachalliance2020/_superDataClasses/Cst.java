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

    Map<String, String> scoutids = new HashMap<String, String>() {{

    }};

    //Saves scout names into shared preferences
    List<String> SCOUT_NAMES = InputManager.getScoutNames();

    Match[] MATCH_LIST = InputManager.getMatchSchedule();

    int FINAL_MATCH = MATCH_LIST.length - 1;

    //Saves scout IDS as 1-18
    List<String> SCOUT_IDS = Arrays.asList("Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3");



    //Stores manual compression keys that aren't separated by commas
    Map<String, String> initialCompressKeys = new HashMap<String, String>() {{
        put("matchInfo", "a");
        put("preload", "b");
        put("isNoShow", "c");
        put("autoMove", "d");
        put("appVersion", "e");
        put("startingLocationY", "g");
        put("startingLocationX", "h");
        put("teleopTime", "i");
    }};

    //Compresses specific keys as letters
    Map<String, String> compressKeys = new HashMap<String, String>() {{
        put("cyclesDefended", "k");
        put("type", "l");
        put("time", "m");

        put("success", "n");
        put("fail", "o");
        put("time", "p");
        put("defended", "q");
        put("self", "r");
        put("bot1", "s");
        put("bot2", "t");
        put("attempted", "u");
        put("climbSuccess", "v");
    }};

    //Compress possible values as certain letters
    Map<String, String> compressValues = new HashMap<String, String>() {{
        put("true", "w");
        put("false", "F");
        put("high", "A");
        put("low", "B");
        put("incap", "S");
        put("unincap", "U");
        put("startDefense", "K");
        put("endDefense", "N");
        put("foul", "Q");
        put("climb", "R");
    }};

    //Compress possible values as certain letters
    Map<String, String> allCompressValues = new HashMap<String, String>() {{
        put("{\"matchInfo\":\"", "a");
        put(",\"preload\":", "b");
        put(",\"isNoShow\":", "c");
        put(",\"autoMove\":", "d");
        put(",\"appVersion\":\"", "e");
        put(",\"startingLocationY\":", "g");
        put("\",\"startingLocationX\":", "h");
        put(",\"teleopTime\":", "i");
        put(",\"fieldOrientation\":", "O");

        put(",\"cyclesDefended\":", "k");
        //put("},{\"type\":", "l");
        put("},{\"type\":", "L");
        put("\",\"actions\":[", "J");

        put(",\"x\":", "x");
        put(",\"y\":", "y");


        put(",\"success\":", "n");
        put(",\"fail\":", "o");
        put(",\"time\":", "p");
        put(",\"defended\":", "q");
        put(",\"self\":{", "r");
        put("},\"bot1\":{", "s");
        put("},\"bot2\":{", "t");
        put("\"attempted\":", "u");
        put(",\"cSuccess\":", "v");

        put("true", "w");
        put("false", "F");
        put("\"high\"", "A");
        put("\"low\"", "B");
        put("\"unincap\"", "C");
        put("\"incap\"", "D");
        put("\"startDefense\"", "E");
        put("\"endDefense\"", "G");
        put("\"foul\"", "H");
        put("\"climb\"", "I");
        put("\"intake\"", "K");
        put("},\"park\":{", "M");
        put(",\"level\":", "O");
    }};

}

package com.example.android.thepeachalliance2020.Managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;

import com.example.android.thepeachalliance2020._superDataClasses.Cst;

public class OutputManager {

    //Compress match data to its corresponding letters for QR generation
    public static String compressMatchData(JSONObject pMatchData) {
        //Get compressed scout name letter
        //getScoutLetter();

        String compressedData = InputManager.matchKey + "|";

        try {
            //Compress specific match header (scout name, match number, etc.)
            JSONArray timeStampData = pMatchData.getJSONArray(InputManager.matchKey);
            Iterator<?> uncompressedKeys = InputManager.mOneTimeMatchData.keys();

            Cst.compressValues.put(InputManager.mScoutName, InputManager.mScoutLetter);

            while (uncompressedKeys.hasNext()) {
                String currentKey = uncompressedKeys.next() + "";
                String currentValue = InputManager.mOneTimeMatchData.get(currentKey) + "";

                if (Cst.initialCompressKeys.containsKey(currentKey) && Cst.compressValues.containsKey(currentValue) && !(currentKey.equals("scoutName") && (InputManager.mScoutLetter + "").equals("null"))) {
                    compressedData = compressedData + Cst.initialCompressKeys.get(currentKey) + Cst.compressValues.get(currentValue) + ",";
                } else if (Cst.initialCompressKeys.containsKey(currentKey) && !(currentValue.equals("") || currentValue.equals("0") || currentValue.equals("null") || (currentKey.equals("scoutName") && (InputManager.mScoutLetter + "").equals("null")))) {
                    compressedData = compressedData + Cst.initialCompressKeys.get(currentKey) + currentValue + ",";
                }
            }

            compressedData = compressedData + "_";

            for (int i = 0; i < timeStampData.length(); i++) {
                //Compress game data for the specific match (intakes, placements, etc.)

                JSONObject tData = timeStampData.getJSONObject(i);

                String compressedDic = "";

                Iterator<?> tDataKeys = tData.keys();

                while (tDataKeys.hasNext()) {
                    String currentKey = tDataKeys.next() + "";
                    String currentValue = tData.get(currentKey) + "";

                    if (Cst.compressKeys.containsKey(currentKey) && Cst.compressValues.containsKey(currentValue)) {
                        compressedDic = compressedDic + Cst.compressKeys.get(currentKey) + Cst.compressValues.get(currentValue);
                    } else if (Cst.compressKeys.containsKey(currentKey)) {
                        compressedDic = compressedDic + Cst.compressKeys.get(currentKey) + currentValue;
                    }
                }
                compressedData = compressedData + compressedDic + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return compressedData;
    }

    public static String compressMatchDataNew(JSONObject pMatchData) {
        String dataSring = pMatchData.toString();
        String dataOut = dataSring;
        for (Map.Entry<String,String> curr : Cst.allCompressValues.entrySet()){
            dataOut = dataOut.replace(curr.getKey(), curr.getValue());
        }
        dataOut = dataOut.replace("{\"type\":", "l");
        //dataOut = dataOut.substring(0, dataOut.length() - 4);
        return dataOut;

    }
}


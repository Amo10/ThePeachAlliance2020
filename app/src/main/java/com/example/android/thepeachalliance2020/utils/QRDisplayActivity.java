package com.example.android.thepeachalliance2020.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020.Managers.OutputManager;
import com.example.android.thepeachalliance2020._superActivities.DialogMaker;

import com.example.android.thepeachalliance2020.MainActivity;

import com.example.android.thepeachalliance2020.R;

public class QRDisplayActivity extends DialogMaker {

    ImageView tQRView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qr_display);
        //Records match information.
        InputManager.initMatchKey();

        tQRView = findViewById(R.id.QRCode_Display);
        //Resets data inputted and fills with match information
        InputManager.mFinalJSON = InputManager.mOneTimeMatchData;
        try {
            InputManager.mFinalJSON.put("actions", (InputManager.mRealTimeMatchData));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Match DATA before COMP", InputManager.mRealTimeMatchData.toString());
        Log.e("Input DATA before COMP", InputManager.mFinalJSON.toString());

        String qrScoutData = OutputManager.compressMatchDataNew(InputManager.mFinalJSON);
        showMatchQR(qrScoutData);
        Log.e("Output DATA after COMP", qrScoutData);

        writeFileOnInternalStorage(("Q" + InputManager.mMatchNum + "_" + new SimpleDateFormat("MM-dd-yyyy-H:mm:ss").format(new Date())), qrScoutData);
    }

    //Calls displayQR to display the QR.
    public void showMatchQR(String qrString) {
        tQRView = (ImageView) findViewById(R.id.QRCode_Display);
        displayQR(qrString);
    }

    //Set QR code parameters and show QR code to send data
    public void displayQR(String qrCode) {
        try {
            //setting size of qr code
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallestDimension = width < height ? width : height;
            //setting parameters for qr code
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCode, charset, hintMap, smallestDimension, smallestDimension);
        } catch (Exception ex) {
            Log.e("QrGenerate", ex.getMessage());
        }
    }

    //Creates QR code dimensions
    public void createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {

        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view
            tQRView.setImageBitmap(null);
            tQRView.setImageBitmap(bitmap);
        } catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
        }
    }

    //Saves scout data as text file in tablet internal storage
    public void writeFileOnInternalStorage(String sFileName, String sBody) {
        //File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/scout_data");
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/scout/matches");        //String filePath = Environment.getExternalStorageDirectory().toString() + "/scout";

        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Takes scout back to Main Activity and increases the match number by 1.
    public void onClickEndScouting(View view) {
        InputManager.mMatchNum++;
        open(MainActivity.class, null, false, false);
    }
}

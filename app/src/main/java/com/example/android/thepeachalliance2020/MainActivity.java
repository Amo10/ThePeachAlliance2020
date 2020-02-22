package com.example.android.thepeachalliance2020;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android.thepeachalliance2020.Managers.InputManager;
import com.example.android.thepeachalliance2020._superActivities.DialogMaker;
import com.example.android.thepeachalliance2020._superDataClasses.Cst;
import com.example.android.thepeachalliance2020.utils.AppUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.thepeachalliance2020.utils.AppUtils.readFile;

public class MainActivity extends DialogMaker {

    public static EditText et_matchNum;
    public static TextView tv_versionNumber, tv_teamNumber;
    public static Spinner sp_triggerScoutNamePopup, sp_triggerTabletIDPopup;

    public Button btn_triggerResendMatches;
    public PopupWindow pw_resendMatchWindow;
    public ListView lv_resendMatch;
    public ImageView QRImage;

    public          ArrayAdapter<String> mResendMatchesArrayAdapter;


    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// Permission has already been granted
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        initAll();

    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

//    private void requestPermits() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getBaseContext(), "Go TO SETTINGS and add permissions", Toast.LENGTH_LONG).show();
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                int test = 0;
//                ActivityCompat.requestPermissions(this,
//                        new String[]{ android.Manifest.permission.READ_EXTERNAL_STORAGE}, test);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//
//               initAll();
//            }
//
//        } else {
//            initAll();
//        }
//    }

    private void initAll(){
        //Set Scout ID from stored data
        //InputManager.mScoutId = AppCc.getSp("scoutId", 0);
        InputManager.getTabletType();
        InputManager.getScoutNames();
        initViews();
        initPopups();
        initListeners();
        //InputManager.recoverUserData();
        updateUserData();
        updateListView();
        listenForResendClick();
    }

    //Set all UI text values
    public static void updateUserData() {
        tv_versionNumber.setText(String.valueOf("Version: " + InputManager.mAppVersion));
        tv_teamNumber.setText(String.valueOf(InputManager.mTeamNum));
        et_matchNum.setText(String.valueOf(InputManager.mMatchNum));


    }


    public void initViews() {
        tv_versionNumber = findViewById(R.id.tv_versionNumber);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        et_matchNum = findViewById(R.id.et_matchNum);


    }
    //Add listeners to map and matchNum editor.
    public void initListeners() {
        et_matchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing, necessary for TextChangedListeners.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing, necessary for TextChangedListeners.
            }

            //Updates match number after altered
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    return;
                }

                int matchNum = AppUtils.StringToInt(editable.toString());

                InputManager.mMatchNum = matchNum;
                InputManager.updateTeamNum();
                tv_teamNumber.setText(String.valueOf(InputManager.mTeamNum));
            }
        });
    }

    //Create the backup, scout name, and ID popups
    public void initPopups() {

        //Declare scout name popup
        sp_triggerScoutNamePopup = (Spinner) findViewById(R.id.btn_triggerScoutNamePopup);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, R.layout.main_popup_header_name, Cst.SCOUT_NAMES);

        sp_triggerScoutNamePopup.setAdapter(nameAdapter);

        sp_triggerScoutNamePopup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mScoutName = sp_triggerScoutNamePopup.getSelectedItem().toString();
                InputManager.mScoutid = Integer.valueOf(Cst.scoutids.get(InputManager.mScoutName));
                //AppCc.setSp("scoutName", InputManager.mScoutName);
                updateUserData();
        }
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

        //Declare Scout ID popup
        sp_triggerTabletIDPopup = (Spinner) findViewById(R.id.btn_triggerTabletIDPopup);
        ArrayAdapter<String> idAdapter = new ArrayAdapter<String>(this, R.layout.main_popup_header_id, Cst.SCOUT_IDS);

        sp_triggerTabletIDPopup.setAdapter(idAdapter);

        sp_triggerTabletIDPopup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mTabletID = sp_triggerTabletIDPopup.getSelectedItem().toString();
                updateUserData();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

        //Declare all resend popups
        btn_triggerResendMatches = (Button) findViewById(R.id.btn_accessData);
        LinearLayout resendMatchesLayout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.main_popup_dropdown_resend, null);
        if (InputManager.mTabletType.equals("fire")) {
            pw_resendMatchWindow = new PopupWindow(resendMatchesLayout, 450, ViewGroup.LayoutParams.MATCH_PARENT, true);
        } else {
            pw_resendMatchWindow = new PopupWindow(resendMatchesLayout, 350, ViewGroup.LayoutParams.MATCH_PARENT, true);
        }
        pw_resendMatchWindow.setBackgroundDrawable(new ColorDrawable());
        lv_resendMatch = resendMatchesLayout.findViewById(R.id.lv_resendMatches);

        mResendMatchesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lv_resendMatch.setAdapter(mResendMatchesArrayAdapter);

        btn_triggerResendMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_resendMatchWindow.showAtLocation((ConstraintLayout) findViewById(R.id.user_layout), Gravity.LEFT,0, 0);
                mResendMatchesArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onClickStartScouting(View view) {
        if (InputManager.mScoutName.equals("unselected") || InputManager.mMatchNum == 0 || InputManager.mTeamNum == 0) {
            Toast.makeText(getBaseContext(), "There is null information!", Toast.LENGTH_SHORT).show();
        }
        else if (InputManager.mMatchNum > Cst.FINAL_MATCH ) {
            //Doesn't let a Scout scout if they have a match number that doesn't exist
            Toast.makeText(getBaseContext(), "Please Input a Valid Match Number", Toast.LENGTH_SHORT).show();
        }
        else {
            //InputManager.initMatchKey();
            open(MapActivity.class, null, false, true);
        }
    }

    //Updates resend dropdown information and formats it
    public void updateListView() {
        final File dir;
        //dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/scout_data");
        dir = new File(Environment.getExternalStorageDirectory().toString()+"/scout/matches/");
        if (!dir.mkdir()) {
            Log.i("File Exists", "Failed to make Directory. Unimportant");
        }

        final File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        mResendMatchesArrayAdapter.clear();

        for (File tmpFile : files) {
            mResendMatchesArrayAdapter.add(tmpFile.getName());
        }

        mResendMatchesArrayAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                File lhsFile = new File(dir, lhs);
                File rhsFile = new File(dir, rhs);
                Date lhsDate = new Date(lhsFile.lastModified());
                Date rhsDate = new Date(rhsFile.lastModified());
                return rhsDate.compareTo(lhsDate);
            }
        });
        mResendMatchesArrayAdapter.notifyDataSetChanged();
    }

    //Opens previous match data when a resend is clicked
    public void listenForResendClick() {
        lv_resendMatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                //name = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/scout_data/" + name;
                name = Environment.getExternalStorageDirectory().toString() + "/scout/matches/" + name;
                final String fileName = name;
                String content = readFile(fileName);
                openQRDialog(content);
            }
        });
    }
    //Creates the layout and opens a QR dialog
    public void openQRDialog(String qrString) {
        final Dialog qrDialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        qrDialog.setCanceledOnTouchOutside(false);
        qrDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final LinearLayout qrDialogLayout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.activity_qr_display, null);

        QRImage = (ImageView) qrDialogLayout.findViewById(R.id.QRCode_Display);

        displayQR(qrString);

        Button ok = (Button) qrDialogLayout.findViewById(R.id.okButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDialog.dismiss();
            }
        });

        qrDialog.setCanceledOnTouchOutside(false);
        qrDialog.setContentView(qrDialogLayout);
        qrDialog.show();
    }
    //Creates dimensions for QR code and opens it
    public void displayQR(String qrCode) {
        try {
            //Set size of QR code.
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);

            int width = point.x;
            int height = point.y;
            int smallestDimension = width < height ? width : height;

            //Set parameters for QR code.
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCode, charset, hintMap, smallestDimension, smallestDimension);
        }
        catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }
    }

    //Sets specifications to create the QR code
    public void createQRCode(String qrCodeData,String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {
        try {
            //Generates qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);

            //Converts bitmatrix to bitmap
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

            //Set bitmap to image view.
            QRImage.setImageBitmap(null);
            QRImage.setImageBitmap(bitmap);
        }
        catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
        }
    }





}
package com.example.android.thepeachalliance2020._superActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.thepeachalliance2020.R;

public class AppTc extends AppCompatActivity {

    //Used to open a new activity
    public void open(Class<? extends Activity> a, Context c, boolean finish, boolean animate) {
        Intent intent = new Intent();

        if (c != null) {
            intent.setClass(c, a);
        } else {
            intent.setClass(this, a);
        }
        startActivity(intent);

        if (finish) {
            finish();
        }

        if (animate) {
            openTransition();
        }
    }

    //Used to do slide transitions when opening a new activity
    public void openTransition() {
        this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void finish() {
        super.finish();
    }
}

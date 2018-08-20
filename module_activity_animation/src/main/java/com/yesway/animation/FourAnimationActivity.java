package com.yesway.animation;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

public class FourAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_animation);
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_center, R.anim.slide_out_to_bottom);
    }
}

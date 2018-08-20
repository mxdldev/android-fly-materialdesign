package com.yesway.shake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/edit/shake/index")
public class MainShakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shake);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(MainShakeActivity.this, R.anim.shake);
                findViewById(R.id.pw).startAnimation(shake);
            }
        });

    }
}

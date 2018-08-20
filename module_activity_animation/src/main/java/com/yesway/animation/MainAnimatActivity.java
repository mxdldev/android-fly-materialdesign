package com.yesway.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/activity/animation/index")
public class MainAnimatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_animat);
        findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAnimatActivity.this,DefaultAnimationActivity.class));
            }
        });
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAnimatActivity.this,FirstAnimationActivity.class));
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAnimatActivity.this,SecondAnimationActivity.class));
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAnimatActivity.this,ThirdAnimationActivity.class));
            }
        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAnimatActivity.this,FourAnimationActivity.class));
            }
        });
    }

}

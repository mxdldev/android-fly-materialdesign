package com.yesway.ripple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/ripple/index")
public class MainActivity extends AppCompatActivity {

    private RippleImageView rippleImageView;
    private Button btn_start,btn_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ripple);
        btn_start=(Button)findViewById(R.id.btn_start);
        btn_stop=(Button)findViewById(R.id.btn_stop);
        rippleImageView=(RippleImageView)findViewById(R.id.rippleImageView);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //停止动画
                rippleImageView.stopWaveAnimation();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始动画
                rippleImageView.startWaveAnimation();
            }
        });

    }
}

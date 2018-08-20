package com.yesway.design;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yesway.animation.MainAnimatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  public static final String TAG = MainActivity.class.getSimpleName();
  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button btn1 = findViewById(R.id.btn1);
    Button btn2 = findViewById(R.id.btn2);
    Button btn3 = findViewById(R.id.btn3);
    Button btn4 = findViewById(R.id.btn4);
    Button btn5 = findViewById(R.id.btn5);
    Button btn51 = findViewById(R.id.btn51);
    Button btn52 = findViewById(R.id.btn52);
    Button btn53 = findViewById(R.id.btn53);
    Button btn6 = findViewById(R.id.btn6);
    Button btn61 = findViewById(R.id.btn61);
    Button btn7 = findViewById(R.id.btn7);
    Button btn8 = findViewById(R.id.btn8);
    Button btn9 = findViewById(R.id.btn9);
    Button btn10 = findViewById(R.id.btn10);
    Button btn11 = findViewById(R.id.btn11);
//    Button btn12 = findViewById(R.id.btn12);
//    Button btn13 = findViewById(R.id.btn13);
//    Button btn14 = findViewById(R.id.btn14);
//    Button btn15 = findViewById(R.id.btn15);
//    Button btn16 = findViewById(R.id.btn16);
    //Button btn17 = findViewById(R.id.btn17);
    Button btn18 = findViewById(R.id.btn18);
    Button btn19 = findViewById(R.id.btn19);
    Button btn20 = findViewById(R.id.btn20);
    btn1.setOnClickListener(this);
    btn2.setOnClickListener(this);
    btn3.setOnClickListener(this);
    btn4.setOnClickListener(this);
    btn5.setOnClickListener(this);
    btn51.setOnClickListener(this);
    btn52.setOnClickListener(this);
    btn53.setOnClickListener(this);
    btn6.setOnClickListener(this);
    btn61.setOnClickListener(this);
    btn7.setOnClickListener(this);
    btn8.setOnClickListener(this);
    btn9.setOnClickListener(this);
    btn10.setOnClickListener(this);
    btn11.setOnClickListener(this);
//    btn12.setOnClickListener(this);
//    btn13.setOnClickListener(this);
//    btn14.setOnClickListener(this);
//    btn15.setOnClickListener(this);
//    btn16.setOnClickListener(this);
    //btn17.setOnClickListener(this);
    btn18.setOnClickListener(this);
    btn19.setOnClickListener(this);
    btn20.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn1:
        ARouter.getInstance().build("/video/index").navigation();
        break;
      case R.id.btn2:
        ARouter.getInstance().build("/imagepager/index").navigation();
        break;
      case R.id.btn3:
        ARouter.getInstance().build("/cheese/index").navigation();
        break;
      case R.id.btn4:
        ARouter.getInstance().build("/banner/index").navigation();
        break;
      case R.id.btn5:
        ARouter.getInstance().build("/recycleview/swipe/index").navigation();
        break;
      case R.id.btn51:
        ARouter.getInstance().build("/channel/manager/main").navigation();
        break;
      case R.id.btn52:
        ARouter.getInstance().build("/desk/menu/index").navigation();
        break;
      case R.id.btn53:
        ARouter.getInstance().build("/tablayout/index").navigation();
        break;
      case R.id.btn6:
        ARouter.getInstance().build("/fragmenttabhost/index").navigation();
        break;
      case R.id.btn61:
        ARouter.getInstance().build("/tabhost/tour/index").navigation();
        break;
      case R.id.btn7:
        ARouter.getInstance().build("/criclemenu/index").navigation();
        break;
      case R.id.btn8:
        ARouter.getInstance().build("/ripple/index").navigation();
        break;
      case R.id.btn9:
        ARouter.getInstance().build("/edit/shake/index").navigation();
        break;
      case R.id.btn10:
        ARouter.getInstance().build("/bookpage/index").navigation();
        break;
      case R.id.btn11:
        ARouter.getInstance().build("/fabmenu/index").navigation();
        break;
      case R.id.btn18:
        ARouter.getInstance().build("/calendar/index").navigation();
        break;
      case R.id.btn19:
        ARouter.getInstance().build("/activity/animation/index").navigation();
        break;
      case R.id.btn20:
        //ARouter.getInstance().build("/transition/index").navigation();
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.lgvalle.material_animations");
        startActivity(LaunchIntent);
        break;
    }
  }
}

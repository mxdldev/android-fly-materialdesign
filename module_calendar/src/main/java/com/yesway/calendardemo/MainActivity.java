package com.yesway.calendardemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.io.File;
@Route(path = "/calendar/index")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);
    }

    public void click(View v) {
       if(v.getId() == R.id.button1){
            startActivity(new Intent(this, MonthViewDemoActivity.class));
        }else if(v.getId() == R.id.button2){
            startActivity(new Intent(this, MonthViewPagerActivity.class));
        }else if(v.getId() == R.id.button4){
            startActivity(new Intent(this, ComplexDemoActivity.class));
        }else if(v.getId() == R.id.button5){
            startActivity(new Intent(this, XiaomiCalendarActivity.class));
        }else if(v.getId() == R.id.button6){
            startActivity(new Intent(this, ScrollerCalendarActivity.class));
        }else if(v.getId() == R.id.button7){
            startActivity(new Intent(this, ScrollerMonthCalendarActivity.class));
        }else if(v.getId() == R.id.button8){
            startActivity(new Intent(this, ScrollerComplexActivity.class));
        }
    }
}

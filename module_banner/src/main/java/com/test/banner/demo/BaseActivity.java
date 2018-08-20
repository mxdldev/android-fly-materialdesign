package com.test.banner.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.test.banner.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    public static List<?> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    public static int H,W;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles= new ArrayList(list1);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        H = dm.heightPixels;
        W = dm.widthPixels;
    }

}

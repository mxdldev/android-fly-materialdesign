package com.test.banner.demo;

import com.test.banner.R;
import com.test.banner.loader.GlideImageLoader;
import com.yesway.design.common.BaseApplication;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class CustomBannerActivity extends BaseActivity {
    Banner banner1,banner2,banner3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_banner);
        banner1 = (Banner) findViewById(R.id.banner1);
        banner2 = (Banner) findViewById(R.id.banner2);
        banner3 = (Banner) findViewById(R.id.banner3);

        banner1.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .start();

        banner2.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .start();

        banner3.setImages(images)
                .setBannerTitles(titles)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .start();
    }
}

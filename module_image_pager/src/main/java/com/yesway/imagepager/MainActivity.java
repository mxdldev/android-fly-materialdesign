package com.yesway.imagepager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mxdl on 2017/12/14.
 */
@Route(path = "/imagepager/index")
public class MainActivity extends AppCompatActivity {

    private ViewPager vip_anfi_functions;
    private LinearLayout lil_anfi_indicators;
    private ImageView imv_anfi_use_now;
    private ImageView[] indicaterViews;
    private boolean isFirst;

    // 启动页传入的广告对象，需要经由此页面传入首页
    //private AdvertInfo mAdvertInfo;
    private final ViewGroup.LayoutParams params_WRAP_CONTENT = new ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagepager_activity_main);
        vip_anfi_functions = (ViewPager) findViewById(R.id.vip_anfi_functions);
        lil_anfi_indicators = (LinearLayout) findViewById(R.id.lil_anfi_indicators);
        imv_anfi_use_now = (ImageView) findViewById(R.id.imv_anfi_use_now);

        vip_anfi_functions.setOnPageChangeListener(new PageChangeListener());
        vip_anfi_functions.setAdapter(new NewFunctionIntroductionAdapter(this));
        isFirst = getIntent().getBooleanExtra("isFirst", false);
        //mAdvertInfo = getIntent().getParcelableExtra(SplashActivity.INTENT_KEY_ADVINFO);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean flag = super.onCreateOptionsMenu(menu);
        //getTitleBar().setVisibility(View.GONE);
        return flag;
    }

    /**
     * 适配器
     */
    private class NewFunctionIntroductionAdapter extends PagerAdapter {
        private final int[] IMAGE_RES_IDS = new int[] {R.mipmap.img_guide_1, R.mipmap.img_guide_2, R.mipmap.img_guide_3};
        private List<ImageView> images;

        public NewFunctionIntroductionAdapter(Context context) {
            images = new ArrayList<ImageView>();

            for (int resId : IMAGE_RES_IDS) {
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(params_WRAP_CONTENT);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(resId);
                images.add(imageView);
            }
            initIndicaterDots(images.size());
        }

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return images.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(images.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }
    }

    /**
     * 滑动横幅监听器
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        // 当当前页面被滑动时调用
        @Override
        public void onPageScrolled(int i, float v, int i2) {}

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int i) {
            // 设置底部小点选中状态
            setIndicaterIndex(i);
        }

        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int i) {}
    }

    /**
     * 初始化索引的小圆点
     */
    private void initIndicaterDots(int size) {
        // 初始化索引指示器的容器Layout
        indicaterViews = new ImageView[size];
        // 循环取得小点图片
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params_WRAP_CONTENT);
            imageView.setImageResource(R.drawable.ic_buy_box_indicator_circle);
            imageView.setEnabled(false);// 都设为灰色
            imageView.setPadding(10, 5, 10, 5);
            indicaterViews[i] = imageView;
            indicaterViews[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
            lil_anfi_indicators.addView(imageView);
        }
        if (indicaterViews.length > 0) indicaterViews[0].setEnabled(true);// 设置为白色，即选中状态
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setIndicaterIndex(int position) {
        if (position < 0 || indicaterViews == null || position > indicaterViews.length - 1) return;
        for (int i = 0; i < indicaterViews.length; i++) {
            if (i == position)
                indicaterViews[i].setEnabled(true);
            else
                indicaterViews[i].setEnabled(false);
        }
        if (position == indicaterViews.length - 1) {
            imv_anfi_use_now.setVisibility(View.VISIBLE);
            lil_anfi_indicators.setVisibility(View.GONE);
        } else {
            imv_anfi_use_now.setVisibility(View.GONE);
            lil_anfi_indicators.setVisibility(View.VISIBLE);
        }
    }
}

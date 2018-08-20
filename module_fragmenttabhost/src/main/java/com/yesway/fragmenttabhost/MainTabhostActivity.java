package com.yesway.fragmenttabhost;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/tabhost/tour/index")
public class MainTabhostActivity extends AppCompatActivity {
    public static final String TAG = MainTabhostActivity.class.getSimpleName();
    private FragmentTabHost mTabHost;
    private Class[] fragments = {MyTourFragment.class,RecomandTourFragment.class};
    private int[] mDrawables =
    {R.drawable.tourrecord_recommandtab_bg, R.drawable.tourrecord_mytab_bg};
    private String[] mTitles = {"精彩路书", "我的路书"};
    private ImageView mImgAddTourRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabhost);
        mImgAddTourRecord = (ImageView) findViewById(R.id.img_add_tourrecord);
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost_tourrecord);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.layout_content);
        mTabHost.getTabWidget().setDividerDrawable(null);
        initTabHost();
    }
    private void initTabHost() {
        mImgAddTourRecord.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int itemPaddingHor = mImgAddTourRecord.getMeasuredWidth() / 2;
        int itemPaddingVer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                getResources().getDisplayMetrics());
        for (int i = 0; i < mTitles.length; i++) {
            LinearLayout view =
                    (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tourrecord_list_tab, null);
            if (i == 0) {
                view.setPadding(0, itemPaddingVer, itemPaddingHor, itemPaddingVer);
            } else {
                view.setPadding(itemPaddingHor, itemPaddingVer, 0, itemPaddingVer);
            }
            ((ImageView) view.findViewById(R.id.img_tab_bg)).setImageResource(mDrawables[i]);
            ((TextView) view.findViewById(R.id.txt_tab_title)).setText(mTitles[i]);

            Bundle bundle = new Bundle();
            bundle.putInt("action", i);// 0:推荐路书；1：我的路书
            mTabHost.addTab(mTabHost.newTabSpec(mTitles[i]).setIndicator(view),
                    fragments[i], bundle);
        }
    }
}

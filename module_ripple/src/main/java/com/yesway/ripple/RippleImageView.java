package com.yesway.ripple;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Description :
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : $(DATE)
 */
public class RippleImageView extends RelativeLayout {

    private static final int SHOW_SPACING_TIME=700;
    private static final int MSG_WAVE2_ANIMATION = 1;
    private static final int MSG_WAVE3_ANIMATION = 2;
    private static final int IMAMGEVIEW_SIZE = 80;
    /**三张波纹图片*/
    private static final int SIZE =3 ;

    /**动画默认循环播放时间*/
    private  int show_spacing_time=SHOW_SPACING_TIME;
    /**初始化动画集*/
    private AnimationSet[] mAnimationSet=new AnimationSet[SIZE];
    /**水波纹图片*/
    private ImageView[] imgs=new ImageView[SIZE];
    /**背景图片*/
    private ImageView img_bg;
    /**水波纹和背景图片的大小*/
    private float imageViewWidth=IMAMGEVIEW_SIZE;
    private float imageViewHeigth=IMAMGEVIEW_SIZE;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    imgs[MSG_WAVE2_ANIMATION].startAnimation(mAnimationSet[MSG_WAVE2_ANIMATION]);
                    break;
                case MSG_WAVE3_ANIMATION:
                    imgs[MSG_WAVE2_ANIMATION].startAnimation(mAnimationSet[MSG_WAVE3_ANIMATION]);
                    break;
            }

        }
    };

    public RippleImageView(Context context) {
        super(context);
        initView(context);
    }

    public RippleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributeSet(context,attrs);
        initView(context);
    }

    /**
     * 获取xml属性
     * @param context
     * @param attrs
     */
    private void getAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custume_ripple_imageview);

        show_spacing_time = typedArray.getInt(R.styleable.custume_ripple_imageview_show_spacing_time, SHOW_SPACING_TIME);
        imageViewWidth = typedArray.getDimension(R.styleable.custume_ripple_imageview_imageViewWidth, IMAMGEVIEW_SIZE);
        imageViewHeigth = typedArray.getDimension(R.styleable.custume_ripple_imageview_imageViewHeigth, IMAMGEVIEW_SIZE);
        Log.d("TAG","show_spacing_time="+show_spacing_time+"mm imageViewWidth="+imageViewWidth+"px  imageViewHeigth="+imageViewHeigth+"px");
        typedArray.recycle();
    }
    private void initView(Context context) {
        setLayout(context);
        for (int i = 0; i <imgs.length ; i++) {
            mAnimationSet[i]=initAnimationSet();
        }
    }

    /**
     * 开始动态布局
     */
    private void setLayout(Context context) {
        LayoutParams params=new LayoutParams(dip2px(context,imageViewWidth),dip2px(context,imageViewHeigth));
        //添加一个规则
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        /**添加水波纹图片*/
        for (int i = 0; i <SIZE ; i++) {
            imgs[i] = new ImageView(context);
            imgs[i].setImageResource(R.mipmap.point_empty);
            addView(imgs[i],params);
        }
        LayoutParams params_bg=new LayoutParams(dip2px(context,imageViewWidth)+10,dip2px(context,imageViewHeigth)+10);
        //添加一个规则
        params_bg.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        /**添加背景图片*/
        img_bg=new ImageView(context);
        img_bg.setImageResource(R.mipmap.point_org);
        addView(img_bg,params_bg);
    }

    /**
     * 初始化动画集
     * @return
     */
    private AnimationSet initAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        //缩放度：变大两倍
        ScaleAnimation sa = new ScaleAnimation(1f, 2f, 1f, 2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(show_spacing_time * 3);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        //透明度
        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        aa.setDuration(show_spacing_time * 3);
        aa.setRepeatCount(Animation.INFINITE);//设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }
    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //============================对外暴露的public方法=========================================
    /**
     * 开始水波纹动画
     */
    public void startWaveAnimation() {
        imgs[0].startAnimation(mAnimationSet[0]);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, show_spacing_time);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, show_spacing_time * 2);
    }

    /**
     * 停止水波纹动画
     */
    public void stopWaveAnimation() {
        for (int i = 0; i <imgs.length ; i++) {
            imgs[i].clearAnimation();
        }
    }
    /**获取播放的速度*/
    public int getShow_spacing_time() {
        return show_spacing_time;
    }
    /**设计播放的速度，默认是800毫秒*/
    public void setShow_spacing_time(int show_spacing_time) {
        this.show_spacing_time = show_spacing_time;
    }

}

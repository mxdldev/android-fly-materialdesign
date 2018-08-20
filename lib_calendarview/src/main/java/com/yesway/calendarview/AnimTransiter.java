package com.yesway.calendarview;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class AnimTransiter {
    private int duration;

    void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * start a custom animation, duration is specified.
     * @param view view
     * @param animation animation
     */
    public void animView(View view, Animation animation) {
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    /**
     * alpha view from 0 to 1, or from 1 to 0.
     * @param view view
     * @param showin true-from 0 to 1, false otherwise.
     */
    public void alphaView(View view, boolean showin) {
        if(view.getVisibility() == View.GONE)
            return;
        AlphaAnimation alphaA = createAlpha(showin);
        view.startAnimation(alphaA);
    }

    /**
     * slide in a view. alpha 0 to 1, and translate vertically.
     * @param view view
     * @param fromTop true - translate -100% to 0, false - translate 100% to 0.
     */
    public void slideInViewVertical(View view, boolean fromTop) {
        slideView(view, true, fromTop, false);
    }

    /**
     * slide out a view. alpha 1 to 0, and translate vertically.
     * @param view view
     * @param fromTop true - translate 0 to 100%, false - translate 0 to -100%.
     */
    public void slideOutViewVertical(View view, boolean fromTop) {
        slideView(view, false, fromTop, false);
    }

    /**
     * slide in a view. alpha 0 to 1, and translate horizontally.
     * @param view view
     * @param fromLeft true - translate -100% to 0, false - translate 100% to 0.
     */
    public void slideInViewHorizontal(View view, boolean fromLeft) {
        slideView(view, true, fromLeft, true);
    }

    /**
     * slide out a view. alpha 1 to 0, and translate vertically.
     * @param view view
     * @param fromLeft true - translate 0 to 100%, false - translate 0 to -100%.
     */
    public void slideOutViewHorizontal(View view, boolean fromLeft) {
        slideView(view, false, fromLeft, true);
    }

    private void slideView(View view, boolean showin, boolean fromTopOrLeft, boolean horizontal) {
        if(view.getVisibility() == View.GONE)
            return;

        AlphaAnimation alphaA = createAlpha(showin);
        TranslateAnimation transA = createTranslate(showin, fromTopOrLeft, horizontal);
        AnimationSet anims = new AnimationSet(false);
        anims.addAnimation(alphaA);
        anims.addAnimation(transA);
        view.startAnimation(anims);
    }

    private AlphaAnimation createAlpha(boolean showin) {
        float start = showin ? 0 : 1;
        float end = showin ? 1 : 0;
        AlphaAnimation animation = new AlphaAnimation(start, end);
        animation.setDuration(duration);
        return animation;
    }

    private TranslateAnimation createTranslate(boolean showin, boolean fromTopOrLeft, boolean horizontal) {
        float start, end;
        if(showin && fromTopOrLeft) {
            start = -1;
            end = 0;
        } else if(showin) {
            start = 1;
            end = 0;
        } else if(fromTopOrLeft) {
            start = 0;
            end = 1;
        } else {
            start = 0;
            end = -1;
        }
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, horizontal ? start : 0,
                Animation.RELATIVE_TO_SELF, horizontal ? end : 0,
                Animation.RELATIVE_TO_SELF, horizontal ? 0 : start,
                Animation.RELATIVE_TO_SELF, horizontal ? 0 : end);
        animation.setDuration(duration);
        return animation;
    }
}

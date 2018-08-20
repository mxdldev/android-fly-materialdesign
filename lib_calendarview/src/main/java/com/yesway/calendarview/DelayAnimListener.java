package com.yesway.calendarview;

import android.animation.Animator;
import android.os.Handler;

public abstract class DelayAnimListener implements Animator.AnimatorListener {
    private int delay;
    private boolean canceled = false;

    /**
     * start
     * @param animator animator
     */
    public abstract void onStart(Animator animator);

    /**
     * after a delay, restart
     * @param animator animator
     */
    public abstract void onContinue(Animator animator);

    /**
     * end
     * @param animator animator
     */
    public abstract void onEnd(Animator animator);

    public DelayAnimListener(int delay) {
        this.delay = delay;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (!canceled) {
            // by cancel method to realize same effect like pause
            animation.cancel();
        } else {
            canceled = false;
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!canceled) {
            onEnd(animation);
        }
    }

    @Override
    public void onAnimationCancel(final Animator animation) {
        canceled = true;

        onStart(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onContinue(animation);
                animation.start();
            }
        }, delay);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

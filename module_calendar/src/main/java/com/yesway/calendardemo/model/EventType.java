package com.yesway.calendardemo.model;

import android.support.annotation.ColorInt;

/**
 * event type enum
 *
 * @author wl
 * @since 2016/08/26 12:09
 */
public enum EventType {
    EAT(0xFFFF6600),
    ENTERTAINMENT(0xFFDC66C0),
    BEAUTY(0xFF66AA76),
    SPORT(0xFF72E6BC);

    private int color;
    EventType(@ColorInt int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}

package com.yesway.calendarview;

import android.content.Context;

public class CommonUtils {
    /**
     * Transform px value into dip or dp
     * 
     * @param pxValue
     * @return dip or dp value
     */ 
    public static int px2dp(Context context, float pxValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (pxValue / scale + 0.5f); 
    } 
   
    /**
     * Transform dip or dp value into px
     * 
     * @param dipValue
     * @return px value
     */ 
    public static int dp2px(Context context, float dipValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dipValue * scale + 0.5f); 
    } 
   
    /**
     * Transform px value into sp value 
     * 
     * @param pxValue
     * @param fontScale
     * @return sp value
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * Transform sp value into px value
     * 
     * @param spValue
     * @param fontScale
     * @return px value
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
}
package com.yesway.calendarview.year;


public class ScrollerYearLunar {
    private int year;
    public ScrollerYearLunar(){
    	
    }
    
    public ScrollerYearLunar(int year){
    	this.year = year;
    }


    final public String animalsYear() {
        final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        return Animals[(year - 4) % 12];
    }

    
    final private static String cyclicalm(int num) {
        final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    final public String cyclical() {
        int num = year - 1900 + 36;
        return (cyclicalm(num));
    }
    
}
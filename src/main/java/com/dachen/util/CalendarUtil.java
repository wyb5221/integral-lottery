package com.dachen.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

    /**
     * 判断当前时间距离第二天凌晨的秒数
     *
     * @return 返回值单位为[s:秒]
     */
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.MILLISECOND, 0);

        Date today = cal.getTime();
        System.out.println("today:"+today);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式
        String dateStr = df.format(cal.getTime());
        System.out.println("dateStr:"+dateStr);

        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    public static void main(String[] args) {
        System.out.println(getSecondsNextEarlyMorning());
    }
}

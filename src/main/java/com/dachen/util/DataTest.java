package com.dachen.util;

import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DataTest {
    public static void main(String[] args) {
        printWeekdays();
    }

    private static final int FIRST_DAY = Calendar.MONDAY;

    private static void printWeekdays() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EE");
        Calendar calendar = Calendar.getInstance();
        System.out.println("1:"+dateFormat.format(calendar.getTime()));

        setToFirstDay(calendar);

        System.out.println("1:"+dateFormat.format(calendar.getTime()));

        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 7; i++) {
            printDay(calendar);
            String format = dateFormat.format(calendar.getTime());
            list.add(format);
            calendar.add(Calendar.DATE, 1);
        }

        System.out.println(list.toArray());
    }

    //根据当前时间获取本周星期一的日期
    private static void setToFirstDay(Calendar calendar) {
        System.out.println("SS:"+calendar.get(Calendar.DAY_OF_WEEK));
        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
            calendar.add(Calendar.DATE, -1);
        }
    }

    private static void printDay(Calendar calendar) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EE");
//        System.out.println(dateFormat.format(calendar.getTime()));

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM.dd");
        System.out.println(dateFormat1.format(calendar.getTime()));
    }
}

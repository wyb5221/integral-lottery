package com.dachen.util;



import com.dachen.common.exception.ServiceException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liangcs
 * @desc
 * @date:2017/9/12 10:08
 * @Copyright (c) 2017, DaChen All Rights Reserved.
 */
public class TimeUtil {

    public static DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public static DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

    public static DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // 获得当天0点时间
    public static Date getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天近30天时间
    public static Date getMonthFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesMorning().getTime() - (30 * 24 * 60 * 60 * 1000L));
        return cal.getTime();
    }


    /**
     * 获取两个Long型的时间差,单位为分钟
     * @param beginTime
     * @param endTime
     * @return
     */
    public static String getDiffMinutes(Long beginTime,Long endTime){
        if (endTime - beginTime < 0){
            return null;
        }

        Long diff = endTime - beginTime;
        final String HOURS = "h";
        final String MINUTES = "min";
        //final String SECONDS = "sec";

        final long MS_IN_A_DAY = 1000 * 60 * 60 * 24;
        final long MS_IN_AN_HOUR = 1000 * 60 * 60;
        final long MS_IN_A_MINUTE = 1000 * 60;
        final long MS_IN_A_SECOND = 1000;
        //Date currentTime = new Date();
        //long numDays = diff / MS_IN_A_DAY;
        //diff = diff % MS_IN_A_DAY;
        long numHours = diff / MS_IN_AN_HOUR;
        diff = diff % MS_IN_AN_HOUR;
        long numMinutes = diff / MS_IN_A_MINUTE;
        diff = diff % MS_IN_A_MINUTE;
        //long numSeconds = diff / MS_IN_A_SECOND;
        diff = diff % MS_IN_A_SECOND;
        //long numMilliseconds = diff;

        StringBuffer buf = new StringBuffer();
        StringBuffer buf1 = new StringBuffer();
        if (numHours > 0) {
            buf.append(numHours);
        }

        if (numMinutes > 0) {
            buf1.append(numMinutes);
        }

        //buf.append(numSeconds + " " + SECONDS);
        String hour = buf.toString();
        String min = buf1.toString();
        int h = 0;
        int m = 0;
        if (hour != null && !hour.isEmpty()){
            h = Integer.parseInt(hour)*60;
        }
        if (min != null && !min.isEmpty()){
            m = Integer.parseInt(min);
        }

        String result = h + m == 0 ?  "1":String.valueOf(h+m);
        return  result;
    }

    /**
     *  date类型转换为String类型
     * formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * data Date类型的时间
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * long类型转换为String类型
     * currentTime要转换的long类型的时间
     * formatType要转换的string类型的时间格式
     */
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * string类型转换为date类型
     * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * long转换为Date类型
     * currentTime要转换的long类型的时间
     * formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * string类型转换为long类型
     * strTime要转换的String类型的时间
     * formatType时间格式
     * strTime的时间格式和formatType的时间格式必须相同
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * date类型转换为long类型
     * date要转换的date类型的时间
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 获取某一天的起点时间
     * @param date
     * @return
     */
    public static long dateBeginToLong(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return  calendar.getTime().getTime();
    }

    /**
     * 获取某一天的结束时间
     * @param date
     * @return
     */
    public static long dateEndToLong(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return  calendar.getTime().getTime();
    }

    /**
     * 获取距离给定的日期之前或之后几天的日期
     * @param date
     * @param diff
     * @param isBefore
     * @return
     */
    public static Date dateBeforeOrAfter(Date date,int diff,boolean isBefore){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int days = calendar.get(Calendar.DATE);
        if (isBefore){
            calendar.set(Calendar.DATE,days-diff);
        }else {
            calendar.set(Calendar.DATE,days+diff);
        }
        return  calendar.getTime();
    }

    public static Date getTimesEveing() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal.getTime();
    }

    public static String getCurrentDateToString(){
        return df2.format(new Date());
    }

    public static Date getFormatDate(Date date){
        String str = df2.format(date);
        Date dd = null;
        try {
              dd = stringToDate(str, "yyyy-MM-dd");
        } catch (ParseException e) {
            throw new ServiceException("日期解析错误!");
        }

        return dd;
    }

    public static String dateToStamp3ToStr(Long time) throws ParseException{
		String res="";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		Date date = new Date(time);
		res = simpleDateFormat.format(date);
		return res;
	}
}

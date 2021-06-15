package com.dachen.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/23 18:17
 * @Description:
 */
public class test {

    public static void main(String[] args) throws ParseException {

        String str = "15800080812";
        System.out.println(DigestUtils.md5Hex("dachen_13471" + str));

//
//        String s = String.valueOf(System.currentTimeMillis() / 1000);
//        System.out.println(s);
////
//        String signature = "name=陈胜良&hospital=上海交通大学医学院附属仁济医院&prov=上海市&timestamp="+s+"&JFfdaf.lHUn?!ghjh;nsjanfjdskalmk213./";
//        signature = DigestUtils.md5Hex(signature).toUpperCase();
//        System.out.println(signature);
//        System.out.println(String.valueOf(System.currentTimeMillis() / 1000));
//        List<Long> dateList = Arrays.asList(2L, 9L, 4L, 3L, 1L, 10L);
////        List<Long> sortIdList = dateList.stream().sorted(Long :: compareTo).collect(Collectors.toList());
//        Collections.sort(dateList);
//        System.out.println(dateList);
//        System.out.println(dateList.get(0));
//        System.out.println(dateList.get(dateList.size()-1));
//        String[] parsePatterns = {"yyyy-MM-dd","yyyy年MM月dd",
//                "yyyy.MM.dd", "yyyy/MM/dd","yy-MM-dd","yy年MM月dd",
//                "yy.MM.dd", "yy/MM/dd", "yyyyMMdd"};
//        //(\\d{4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2})
//        String str = "210606,21.06.06,21.6.6,21-06-06,21-6-6,21/6/6,21/06/06斤斤计较2021.05.25测试2021-05-20时间2021/05/21格式2021年5月22日，车的得到的2020-5-21哈哈哈2020/5/21啦啦啦啦啦2020.5.21,,2020.5.1";
       /* String str = "112021-6-21111";
        String regEx = "((\\d{2,4})[-|\\/|年|\\.|](\\d{1,2})[-|\\/|月|\\.|]([1-9]|0[1-9]|[12][0-9]))";//正则表达式
        Pattern p = Pattern.compile(regEx);//获取正则表达式中的分组，每一组小括号为一组

        Matcher m = p.matcher(str);//进行匹配
        System.out.println(m);
        while(m.find()) {
            System.out.println(m.group());

            Date date = DateUtils.parseDate(m.group(), parsePatterns);
            long time = date.getTime();
            System.out.println(time);
        }*/

//        Pattern p2 = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//
//        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
//
//        String a = ",,,20210609";
//        String regEx1 = "(\\d[20]\\d{6})$";//正则表达式
//        Pattern p1 = Pattern.compile(regEx1);//获取正则表达式中的分组，每一组小括号为一组
//        Matcher m1 = p1.matcher(a);//进行匹配
//        System.out.println(m1);
//        while(m1.find()) {
//            System.out.println(m1.group());
//
//            Date date = DateUtils.parseDate(m1.group(), parsePatterns);
//            long time = date.getTime();
//            System.out.println(time);
//        }

//        if (m.find()) {//判断正则表达式是否匹配到
//            System.out.println(m.group(0));
//            importFileName = m.group(0);//通过group来获取每个分组的值，group(0)代表正则表达式匹配到的所有内容，1代表第一个分组
//           importFiles.add(importFileName);
//            //System.out.println("引入文件是:"+ importFileName);
//        }

    }
}

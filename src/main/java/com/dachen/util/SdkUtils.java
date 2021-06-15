package com.dachen.util;


import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SdkUtils {

    private static final char[] charArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y',
            'z'};

    public static String getSexStr(short sex) {
        // 1男，2女
        switch (sex) {
            case 1:
                return "男";
            case 2:
                return "女";
        }
        return "未知";
    }

    private static final Pattern Pattern_mobile = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$", Pattern.CASE_INSENSITIVE);

    public static boolean isMobileNum(String mobile) {
        Matcher m = Pattern_mobile.matcher(mobile);
        return m.matches();
    }

    public static String toString(Map<String, String[]> params) {
        if (isEmpty(params)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, String[]> entry:params.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(Arrays.toString(entry.getValue()));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        return sb.toString();
    }

    public static String randomUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        return uuidStr;
    }

    public static String buildQueryString(Map<String,String> params){
        return buildQueryString(params, false); // 默认保存原来的顺序
    }

    public static String buildQueryString(Map<String,String> params, boolean sort){
        if (isEmpty(params)) {
            return "";
        }

        if (sort) {
            Map<String, String> sortParams = new TreeMap<>(String::compareToIgnoreCase);
            sortParams.putAll(params);

            params = sortParams;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<String> key = params.keySet().iterator();
        while(key.hasNext()){
            String k = key.next();
            String v = params.get(k);
            try {
                v = java.net.URLEncoder.encode(v, "UTF-8"); // 参数url编码
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append(String.format("&%s=%s", k,null == v ? "" : v));
        }

        sb.delete(0, 1); // 删除开头的&字符

        return sb.toString();
    }

    public static String getRequestString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestURI：" + request.getRequestURI());
        sb.append("\n");

        Map<String, String[]> paramMap = request.getParameterMap();
        if (!paramMap.isEmpty()) {
            sb.append("Params：");
            Set<Map.Entry<String, String[]>> set = paramMap.entrySet();
            Iterator<Map.Entry<String, String[]>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String[]> entry = iterator.next();
                sb.append(entry.getKey());
                sb.append("=");
                if (null == entry.getValue() || 0 == entry.getValue().length) {
                    sb.append("");
                } else if (1 == entry.getValue().length){
                    sb.append(entry.getValue()[0].toString());
                } else {
                    sb.append(Arrays.toString(entry.getValue()));
                }
                sb.append("&");
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        sb.append("Method：" + request.getMethod());
        sb.append("\n");

        sb.append("Content-Type：" + request.getContentType());
        sb.append("\n");

        sb.append("Accept：" + request.getHeader("Accept"));
        sb.append("\n");

        sb.append("User-Agent：" + request.getHeader("User-Agent"));
        sb.append("\n");

//        sb.append("Request body：" + charReader(request));
//        sb.append("\n");

        sb.append("**************************************************");
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !(isEmpty(collection));
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static <T> boolean isEmpty(T[] arr) {
        if (null == arr || 0 == arr.length) {
            return true;
        }
        return false;
    }

    public static <T> boolean isNotEmpty(T[] arr) {
        return !isEmpty(arr);
    }

    public static <T> List<T> reduceIncluded(Collection<T> collection, Collection<T> includedItems) {
        if (isEmpty(collection) || isEmpty(includedItems)) {
            return null;
        }
        List<T> reduceItems = new ArrayList<>();
        for (T includedItem:includedItems) {
            Iterator<T> iterator = collection.iterator();
            while (iterator.hasNext()) {
                T o = iterator.next();
                if (includedItem.equals(o)) {
                    reduceItems.add(o);
                    iterator.remove();
                    break;
                }
            }
        }
        return reduceItems;
    }

    public static String format(long number, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumIntegerDigits(length);
        nf.setMinimumIntegerDigits(length);
        nf.setGroupingUsed(false);
        return nf.format(number);
    }

    public static String randomNum(int length) {
        int ran = ThreadLocalRandom.current().nextInt(Double.valueOf(Math.pow(10, length)).intValue());
        return format(ran, length);
    }

    public static String randomNum() {
        return randomNum(6);
    }

    public static String random(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<length; i++) {
            sb.append(charArray[ThreadLocalRandom.current().nextInt(charArray.length)]);
        }
        String ran = sb.toString();
        return ran;
    }

    public static void main(String[] args) throws InterruptedException {
        while(true) {
            String ran = random(6);
            System.out.println(ran);
            Thread.sleep(1000L);
        }
    }
}

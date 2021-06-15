package com.dachen.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象与map互转
 * Created by xiejs on 2018/1/24.
 */
public class ObjectMapUtils {

    /**
     * map 转对象
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) {
        if (map == null){
            return null;
        }
        try{
            T obj = beanClass.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    setter.invoke(obj, map.get(property.getName()));
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象转map
     * @param t
     * @return
     * @throws Exception
     */
    public static <T> Map<String, Object> objectToMap(T t){
        if (t == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
       try{
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(t) : null;
                map.put(key, value);
            }
           return map;
        } catch (Exception e) {
            e.printStackTrace();
           return null;
        }
    }

    /**
     * 对象转map(list 集合)
     * @param tList
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> objectToMap(List<T> tList){
        if (tList == null){
            return null;
        }
        List<Map<String, Object>> rsList = new ArrayList();
        for(T t:tList){
            if(t==null){
                continue;
            }
            Map<String, Object> map = objectToMap(t);
            if(map==null){
                continue;
            }
            rsList.add(map);
        }
        return rsList;
    }
}

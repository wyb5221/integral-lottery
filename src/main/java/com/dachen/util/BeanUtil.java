package com.dachen.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtil {

    class Room {
        private String name;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
    public static void main(String[] args) {
        BeanUtil beanUtil = new BeanUtil();
        Room room = beanUtil.new Room();
        room.setName("XP");

        System.out.println(toMap(room));
    }


    public static <T> T copy(Object poObj, final Class<T> voClass) {
        T voObj = null;
        try {
            voObj = voClass.newInstance();
            BeanUtils.copyProperties(poObj, voObj);
            return voObj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> copyList(List<? extends Object> poList, final Class<T> voClass) {

        List<T> voList = new ArrayList<>();

        T voObj = null;
        for (Object poObj : poList) {
            try {
                voObj = voClass.newInstance();
                BeanUtils.copyProperties(poObj, voObj);
                voList.add(voObj);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return voList;
    }

    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class clazz = obj.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        map.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
        return map;
    }
}

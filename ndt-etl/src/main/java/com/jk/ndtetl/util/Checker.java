package com.jk.ndtetl.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.jk.ndtetl.system.User;

/**
 * 
 * 对常用的null和空内容的检查工具
 * 
 * @author wadefang
 * @version [1.0, 2015年9月23日]
 * 
 */
public class Checker {
    /**
     * 
     * 检查集合对象的索引是否越界，越界返回true
     * 
     * @param obj
     * @return
     */
    public static boolean isIndexOutOfBounds(final Object obj, final int index) {
        if (index < 0) {
            return true;
        }
        if (obj.getClass().isArray()) {
            return index >= ((Object[]) obj).length;
        }
        if (obj instanceof Collection) {
            return index >= ((Collection<?>) obj).size();
        }
        return false;
    }

    /**
     * 
     * 判断一个对象是否为null和空内容，空内容包括空字符串，集合中内容为空。 当checkEmpty为true是，对空内容进行检查
     * 
     * @param obj
     * @param checkEmpty
     * @return
     */
    public static boolean isNullOrEmpty(final Object obj, final boolean checkEmpty) {
        if (obj == null) {
            return true;
        }
        if (!checkEmpty) {
            return false;
        }
        if (obj instanceof String) {
            return StringUtils.isEmpty(((String) obj).trim());
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (!obj.getClass().isArray()) {
            return false;
        }

        // check object array

        return isArrayEmpty(obj);
    }

    protected static boolean isArrayEmpty(final Object obj) {

        final Object[] objects = (Object[]) obj;
        if (objects.length == 0) {
            return true;
        }
        for (int i = 0; i < objects.length; i++) {
            if (!isNullOrEmpty(objects[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(final Object obj) {
        return isNullOrEmpty(obj, true);
    }

    public static boolean isNotNullOrEmpty(final Object obj) {
        return !isNullOrEmpty(obj, true);
    }


    public static boolean checkPassword(String pwd) {
        return Pattern.matches(User.REG_PASSWORD, pwd);
    }
    
    public  static boolean isNum(String str){
		for(int i=str.length();--i>=0;){
			int chr=str.charAt(i);
			if(chr<48||chr>57)
			return false;
		}
		return true;
    }

    public static void main(String[] args) {
        System.out.println( checkPassword("123qweQ1235"));;
    }
}

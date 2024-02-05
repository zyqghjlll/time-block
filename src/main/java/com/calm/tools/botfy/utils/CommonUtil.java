package com.calm.tools.botfy.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zyq
 */
public class CommonUtil {
    private CommonUtil() {
        throw new IllegalStateException("Utility class");
    }

    public  static Long getId() {
        return IdUtil.getSnowflake().nextId();
    }

    public  static Date getDate() {
        return getFormat(new Date());
    }

    public  static String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(new Date());
    }

    public  static Date getFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return DateUtil.parse(dateFormat.format(date));
    }

    public static Date formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return DateUtil.parse(dateFormat.format(date));
    }


    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date createDate(String date, String format) {
        Date result = null;
        if (ObjectUtil.isEmpty(date) || ObjectUtil.isEmpty(format)) {
            throw new RuntimeException("params can not be null");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            result = dateFormat.parse(date);
        } catch (Exception ex) {
            throw new RuntimeException("A exception occurred when format the date string");
        }
        return result;
    }

    public static int getCurrentYear() {
        return DateUtil.year(DateUtil.date());
    }

    public static int getYear(Date date) {
        return DateUtil.year(date);
    }
}

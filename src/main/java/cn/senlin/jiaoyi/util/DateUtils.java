package cn.senlin.jiaoyi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author swu
 * @date 2020-05-20
 */
public class DateUtils {

    /**
     * @return 当前时间
     */
    public static Date date() {
        return new Date();
    }

    /**
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String now() {
        return formatDateTime(new Date());
    }

    /**
     * 当前时间，格式 yyyy-MM-dd
     *
     * @return 当前时间的日期形式字符串
     */
    public static String nowDate() {
        return formatDate(new Date());
    }

    /**
     * 格式化日期时间<br>
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatDateTime(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DateConstant.NORM_DATETIME);
        return formatter.format(date);
    }

    /**
     * 格式化日期部分（不包括时间）<br>
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DateConstant.NORM_DATE);
        return formatter.format(date);
    }

    /**
     * 将字符串转化为Date(可以识别yyyy-MM-dd/yyyy-MM-dd HH:mm:ss/yyyy-MM-dd'T'HH:mm:ss'Z')
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date parse(String str) {
        if (null == str) {
            return null;
        }
        String pattern;
        if (str.length() == 10) {
            pattern = "yyyy-MM-dd";
        } else if (str.length() == 7) {
            pattern = "yyyy-MM";
        } else {
            if (str.contains("T")) {
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            } else {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }
        }
        return parse(str, pattern);
    }

    /**
     * 将特定格式的日期转换为Date对象
     *
     * @param dateStr 特定格式的日期
     * @param format  格式，例如yyyy-MM-dd
     * @return 日期对象
     */
    public static Date parse(String dateStr, String format) {
        if (null == dateStr) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}

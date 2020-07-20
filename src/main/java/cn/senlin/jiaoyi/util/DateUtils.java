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

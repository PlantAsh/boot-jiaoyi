package cn.senlin.jiaoyi.util;

import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    /**
     * 字符串"yyyy-MM-dd HH:mm:ss"日期转成字符串(long类型形式)日期
     */
    public static Long strToLongNew(String str) {
        try {
            Date date = strToDate(str);
            return date.getTime();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    /**
     * 日期时间"yyyyMMdd","yyyy/MM/dd","yyyy.MM.dd"等格式化成"yyyy-MM-dd"
     *
     * @param
     */
    public static String formatDate2Str(String date) {
        String result;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            return "";
        }
        try {
            result = sdf.format(sdf.parse(date));
            return result;
        } catch (ParseException e) {
            try {
                result = sdf.format(sdf2.parse(date));
                return result;
            } catch (ParseException b) {
                try {
                    result = sdf.format(sdf3.parse(date));
                    return result;
                } catch (ParseException c) {
                    try {
                        result = sdf.format(sdf1.parse(date));
                        return result;
                    } catch (ParseException d) {
                        result = "";
                    }
                }
            }
        }
        return result;
    }


    /**
     * 延后几天的日期
     *
     * @param curr
     * @param nextDays
     * @return
     */
    public static Date nextDate(Date curr, int nextDays) {
        if (curr == null) {
            return null;
        }
        return new Date(curr.getTime() + (long) nextDays * 24 * 60 * 60 * 1000);
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @return
     */
    public static String toDateStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }


    /**
     * 字符串转化成日期格式 "yyyy-MM-dd'T'HH:mm:ss"
     */
    public static Date strToDate(String str) {

        SimpleDateFormat sdf;
        if (str.length() == 10) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }else if(str.length() == 8){
            sdf = new SimpleDateFormat("HH:mm:ss");
        }
        else {
            if (str.contains("T")){
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            } else{
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        }
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 按格式获取当前时间
     *
     * @param format
     * @return
     */
    public static String getNowDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }


    public static String toFullDateStr(Date date) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }

    /**
     * 日期转字符串
     *
     * @param time
     * @return
     */
    public static String toDateTimeStr(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    /**
     * 日期转字符串
     *
     * @param time
     * @return
     */
    public static String toYearMonthDayStr(long time) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(time));
    }

    /**
     * 获取当前系统时间,返回Timestamp格式的时间("yyyy-MM-dd HH:mm:ss")
     *
     * @return Timestamp
     */
    public static Timestamp getNowTime() {
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return Timestamp.valueOf(nowTime);
    }

    /**
     * 获取当前系统日期  yyyyMMdd
     */
    public static String getNowDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 获取当前系统时间  yyyy-mm-dd hh:mm:ss
     */
    public static String getNowTimeStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 字符串转日期时间
     *
     * @param str
     * @return
     */
    public static Timestamp toTime(String str) {
        if (str != null && str.length() > 0) {
            if (!str.contains(":")){
                str = str.trim() + " 00:00:00";
            }
            return Timestamp.valueOf(str);
        } else {
            return null;
        }
    }

    /**
     * 字符串转日期时间
     *
     * @param str
     * @param isMorning 是否一天最早，反之一天最晚
     * @return
     */
    public static Timestamp toTime(String str, boolean isMorning) {
        //不含时间
        if (!str.contains(":")) {
            if (isMorning){
                str = str.trim() + " 00:00:00";
            } else {
                str = str.trim() + " 23:59:59";
            }
        }
        return Timestamp.valueOf(str);
    }

    /**
     * 获取两个日期之间所有的日期
     *
     * @param date1
     * @param date2
     * @return
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static List<String> days(String date1, String date2) {
        List<String> L = new ArrayList<>();
        if (date1 == null || date2 == null || date1.length() == 0 || date2.length() == 0) {
            return L;
        }
        String tmp;
        // 确保 date1的日期不晚于date2
        if (date1.compareTo(date2) > 0) {
            tmp = date1;
            date1 = date2;
            date2 = tmp;
        }
        tmp = sdf.format(str2Date(date1).getTime());
        while (tmp.compareTo(date2) <= 0) {
            L.add(tmp);
            tmp = sdf.format(str2Date(tmp).getTime() + 3600 * 24 * 1000);
        }
        return L;
    }

    public static Date str2Date(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            //
        }
        return null;
    }

    /**
     * Timestamp2String
     */
    public static String timestamp2String(Timestamp timestamp) {
        if (timestamp != null) {
            try {
                return sdf.format(timestamp);
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    /**
     * Timestamp2String
     * format : yyyy-MM-dd HH:mm:ss
     */
    public static String timestamp2String(Timestamp timestamp, String format) {
        if (timestamp != null) {
            try {
                return new SimpleDateFormat(format).format(timestamp);
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    /**
     * 日期加减天数返回计算后的日期 yyyy-MM-dd
     */
    public static Date addDate(Date date, int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //让日期加1
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + addDay);
        return calendar.getTime();
    }

    /**
     * 日期加减天数返回计算后的日期 yyyy-MM-dd
     */
    public static String addDate(String date, int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(str2Date(date));
        //让日期加1
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + addDay);
        return sdf.format(calendar.getTime());
    }

    /**
     * 日期加减月数返回计算后的日期 yyyy-MM-dd
     */
    public static String addMonth(String date, int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(str2Date(date));
        //让日期加1
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + addMonth);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前系统日期 yyyy-MM-dd
     */
    public static String getNowDate() {
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());
    }

    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 根据生日计算年龄
     */
    public static String getAgeByBirthday(String birthday) {
        Date birthDate = str2Date(birthday);
        if (birthDate == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDate)) {
            return "";
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDate);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return String.valueOf(age);
    }

    /**
     * 计算两个时间的时间差
     *
     * @param d1
     * @param d2
     */
    public static long getTimeDifference(String d1, String d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            return (date2.getTime() - date1.getTime()) / 1000;
        } catch (ParseException ignored) {

        }
        return 0;
    }

    /**
     * 计算两个时间的时间差 (秒)
     *
     * @param d1
     * @param d2
     */
    public static long getTimeDiff(String d1, String d2, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            return (date2.getTime() - date1.getTime()) / 1000;
        } catch (ParseException ignored) {

        }
        return 0;
    }

    /**
     * 判断当前系统时间是否在时间范围内
     */
    public static boolean checkInTwoTimes(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = sdf.parse(addBeginTime(start));
            Date date2 = sdf.parse(addEndTime(end));
            Date now = new Date();
            if ((now.getTime() >= date1.getTime()) && (now.getTime() <= date2.getTime())) {
                return true;
            }
        } catch (ParseException ignored) {

        }
        return false;
    }

    public static boolean checkInTwoTimes(Date start, Date end, Date mid) {
        return (mid.getTime() >= start.getTime()) && (mid.getTime() <= end.getTime());
    }

    /**
     * 两个日期比大小 d1>d2=-1 d1=d2=0 d1<d2=1 ;
     */
    public static int compare2Date(String d1, String d2) {
        String format;
        if (d1.length() > 11) {
            format = "yyyy-MM-dd HH:mm:ss";
        } else {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            return Long.compare(date2.getTime(), date1.getTime());
        } catch (ParseException e) {
            return -9;
        }
    }

    /**
     * 获取当前系统年份
     */
    public static int getSysYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getSysYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前系统月份
     */
    public static int getSysMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getSysMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 日期时间"yyyyMMddHHmmss"格式化成"yyyy-MM-dd HH:mm:ss"
     *
     * @param time
     */
    public static String formatTime2Str(String time) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (time != null) {
                result = sdf2.format(sdf.parse(time));
            }
        } catch (ParseException e) {
            result = "";
        }
        return result;
    }

    /**
     * 补上时分秒 00:00:00
     *
     * @param time
     * @return
     */
    public static String addBeginTime(String time) {
        if (!time.contains(":")) {
            time = time.trim() + " 00:00:00";
        }
        return time;
    }

    /**
     * 补上时分秒 23:59:59
     *
     * @param time
     * @return
     */
    public static String addEndTime(String time) {
        if (!time.contains(":")) {
            time = time.trim() + " 23:59:59";
        }
        return time;
    }

    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 计算两个时间的时间差
     *
     * @param d1
     * @param d2
     * @param dateFormate
     * @return
     */
    public static long getDays(String d1, String d2, String dateFormate) {
        if (dateFormate == null) {
            dateFormate = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormate);
        try {
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            return (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24);
        } catch (ParseException ignored) {

        }
        return 0;
    }

    /**
     * yyyy年MM月dd日
     *
     * @return
     */
    public static String getNowDayStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前的时间 以秒为单位
     *
     * @return
     */
    public static long getLongNowDayStr() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前系统时间,返回字符串格式的时间("yyyy-MM-dd HH:mm:ss")
     *
     * @return Timestamp
     */
    public static String getStrNowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    // 获得当天0点时间
    public static Date getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天23:59:59时间
    public static Date getTimesNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得本周一0点时间
    public static Date getTimesWeekMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    // 获得本周日23:59:59时间
    public static Date getTimesWeekNight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekMorning());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public static Date getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天23:59:59时间
    public static Date getTimesMonthNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    // 获得本年初0点时间
    public static Date getTimesYearMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
        return cal.getTime();
    }

    //获得本年末23:59:59时间
    public static Date getTimesYearNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    /**
     * 两个时间相差距离 多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为： xx小时xx分xx秒
     */
    public static String getDateDiff(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            long time1 = df.parse(str1).getTime();
            long time2 = df.parse(str2).getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            hour = (diff / (60 * 60 * 1000));
            min = ((diff / (60 * 1000)) - hour * 60);
            sec = (diff / 1000 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hour + "小时" + min + "分" + sec + "秒";
    }

    /**
     * 某个时间加上 分钟 和秒
     *
     * @param startTime 2017-7-15 09:57:53
     * @param min       分钟
     * @param sec       秒
     * @return 2017-7-15 09:58:08
     */
    public static String andMinite(String startTime, int min, int sec) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dfs.parse(startTime));
            calendar.add(Calendar.MINUTE, min);
            calendar.add(Calendar.SECOND, sec);
            return dfs.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param scheduleHour 日程时间
     * @param advanceTime  提前时间(小时)
     * @author: xuk
     * @date: 2017/8/29
     */
    public static String getSendHour(Integer scheduleHour, Integer advanceTime) {

        int i = advanceTime % 24;
        int hour = scheduleHour - i;
        hour = hour < 0 ? 24 + hour : hour;
        return String.valueOf(hour);
    }

    /**
     * 根据身份证号获取生日
     *
     * @author: xuk
     * @date: 2017/9/20
     */
    public static String getBirthDateByIdCard(String idCard) {
        if (StringUtils.isEmpty(idCard)) {return null;}
        String dates;
        try {
            dates = idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dates;
    }

    /**
     * 计算过去某个时期距离今天的天数
     *
     * @author: jjwu
     * @date: 2017/11/02
     */
    public static int getDaysPast2Now(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateString);
            return (int) ((System.currentTimeMillis() - date.getTime()) / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 根据年月日计算总天数（参数月可以大于12，日可以大于31）
     *
     * @author: jjwu
     * @date: 2017/11/09
     */
    public static Integer daysByYYMMDD(Integer years, Integer months, Integer days) {
        if (years == null && months == null && days == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (years != null) {
            calendar.add(Calendar.YEAR, -years);
        }
        if (months != null) {
            calendar.add(Calendar.MONTH, -months);
        }
        if (days != null) {
            calendar.add(Calendar.DAY_OF_YEAR, -days);
        }
        String s = toDateStr(calendar.getTime());
        return getDaysPast2Now(s);
    }
}

package com.ddtsdk.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by CZG on 2020/4/3
 * 时间工具
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_MONTH_DAY = "MM月dd日 HH:mm";
    public final static String FORMAT_MONTH = "MM月dd日";

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM-dd HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME2 = "MM-dd HH:mm:ss";

    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE1_TIME = "yyyy年MM月dd日 HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMAT_DATA_YMD = "yyyy年MM月dd日";

    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 946080000;// 年 365 * 24 * 60 * 60
    private static final int MONTH = 2592000;// 月 30 * 24 * 60 * 60
    private static final int DAY = 86400;// 天 24 * 60 * 60
    private static final int HOUR = 3600;// 小时
    private static final int MINUTE = 60;// 分钟

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前， 七天前显示时间,去年显示年月日时分
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp7(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr;

        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        if (calendar.get(Calendar.YEAR) < curCalendar.get(Calendar.YEAR)) {
            return TimeUtil.longToString(timestamp, FORMAT_DATA_YMD);
        }
        if (timeGap > 7 * DAY) {
            return TimeUtil.longToString(timestamp, FORMAT_DATA_YMD);
        }
        if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    public static String getDayTimeSub(long systime, long mTime, String sub) {
        long time = systime -mTime;
        long s = time / 1000;
        long m = s / 60;
        long mm = m % 60;
        long h = m / 60;
        long hh = h % 24;
        long dd = h / 24;
        if (dd > 0) {
            if (dd < 7) {
                return dd + "天" + sub;
            } else {
                return TimeUtil.longToString(mTime, TimeUtil.FORMAT_DATA_YMD);
            }
        } else if (hh > 0) {
            return hh + "小时" + sub;
        } else if (mm > 0) {
            return mm + "分钟" + sub;
        } else {
            return "刚刚";
        }
    }
    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) {
        String strTime = "";
        Date date = longToDate(currentTime, formatType);// long类型转成Date类型
        strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getMinAndSec(long time) {
        if (time < 60)
            return time + "\"";
        else {
            long min = time / 60;
            long sec = time % 60;
            return min + "\'" + sec + "\'\'";
        }
    }

    /**
     * 获取聊天时间：因为sdk的时间默认到秒故应该乘1000
     *
     * @param @param  timesamp
     * @param @return
     * @return String
     * @throws
     * @Title: getChatTime
     */
    public static String getChatTime(long timesamp) {
        long clearTime = timesamp;
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;

            default:
                result = getTime(clearTime);
                break;
        }

        return result;
    }

    /**
     * 判断时间是否是过去时间
     *
     * @param oldTime
     * @return
     */
    public static long cdTime(String oldTime) {
        long time = Long.parseLong(oldTime);
        long currentTime = System.currentTimeMillis();
        return time - currentTime;
    }

    /**
     * 判断课程开始时间是否大于现在时间两小时
     *
     * @param startTime
     * @return
     */
    public static boolean isMore2Hour(String startTime) {
        long time = Long.parseLong(startTime);
        long currentTime = System.currentTimeMillis();
        return currentTime - time >= 7200000;
    }

    public static String getDateHourTime(long time) {
        long s = time / 1000;
        long ss = s % 60;
        long m = s / 60;
        long mm = m % 60;
        long h = m / 60;
        long hh = h % 24;
        long dd = h / 24;
        StringBuffer timeBuffer = new StringBuffer();
        if (dd > 9) {
            timeBuffer.append(dd + "天 ");
        } else {
            timeBuffer.append("0" + dd + "天 ");
        }
        if (hh > 9) {
            timeBuffer.append(hh + "时 ");
        } else {
            timeBuffer.append("0" + hh + "时 ");
        }
        if (mm > 9) {
            timeBuffer.append(mm + "分 ");
        } else {
            timeBuffer.append("0" + mm + "分 ");
        }
        if (ss > 9) {
            timeBuffer.append(ss + "秒 ");
        } else {
            timeBuffer.append("0" + ss + "秒 ");
        }
        return timeBuffer.toString();
    }

    public static String getDayTime(long time) {
        long s = time / 1000;
        long m = s / 60;
        long mm = m % 60;
        long h = m / 60;
        long hh = h % 24;
        long dd = h / 24;
        if (dd > 0) {
            return dd + "天";
        } else if (hh > 0) {
            return hh + "小时";
        } else if (mm > 0) {
            return mm + "分钟";
        } else {
            return "刚刚";
        }
    }


    public static List<String> getCountDownTime(long time) {
        long s = time / 1000;
        long ss = s % 60;
        long m = s / 60;
        long mm = m % 60;
        long h = m / 60;
        long hh = h % 24;
        long dd = h / 24;
        List<String> timeList = new ArrayList<>();
        timeList.add(dd + "");
        if (hh > 9) {
            timeList.add(hh + "");
        } else {
            timeList.add("0" + hh);
        }
        if (mm > 9) {
            timeList.add(mm + "");
        } else {
            timeList.add("0" + mm);
        }
        if (ss > 9) {
            timeList.add(ss + "");
        } else {
            timeList.add("0" + ss);
        }
        return timeList;
    }

    public static String changeFormatTime(String time) {
        Date date;
        String str = "";
        try {
            long lt = Long.parseLong(time);
            date = new Date(lt);
            sdf = new SimpleDateFormat(FORMAT_DATE1_TIME);
            str = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String timeStampToDate(long timeStamp) {
        String dateTime = "";
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            dateTime = sd.format(new Date(timeStamp));
        } catch (NumberFormatException e) {
            dateTime = "00:00:00";
            Log.e("", "timeStampToDate转换异常");
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * 判断是否是新的一天
     * <p>
     * return true:是新的一天 false:不是新的一天
     */
    public static boolean isNewDay(long oldTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();

        String oldTime = "";
        String currentTime = "";
        try {
            SimpleDateFormat sd = new SimpleDateFormat(FORMAT_DATE);
            oldTime = sd.format(new Date(oldTimeMillis));
            currentTime = sd.format(new Date(currentTimeMillis));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String[] oldSplit = oldTime.split("-");
        String[] currentSplit = currentTime.split("-");

        if (oldSplit.length <= 0 || currentSplit.length <= 0) {
            return false;
        } else if (Integer.valueOf(currentSplit[0]) > Integer.valueOf(oldSplit[0])) {
            return true;
        } else if (Integer.valueOf(currentSplit[1]) > Integer.valueOf(oldSplit[1])) {
            return true;
        } else return Integer.valueOf(currentSplit[2]) > Integer.valueOf(oldSplit[2]);
    }

    public static String compareDate(String futureDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        long nowDate = calendar.getTime().getTime(); //Date.getTime() 获得毫秒型日期

        try {
            long specialDate = sdf.parse(futureDate).getTime();
            long betweenDate = (specialDate - nowDate) / (1000 * 60 * 60 * 24);
            if (betweenDate > 7) {
                return betweenDate + "天后更新";
            } else {

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String compareCurrentDateToString(long time) {
        Calendar calendar = Calendar.getInstance();
        Date thisDate = calendar.getTime();
        Date thisSunday = getNextWeekSunday(thisDate);
        long nowDate = longToDate(thisDate.getTime(), "yyyy-MM-dd").getTime();
        long thisSundayDate = longToDate(thisSunday.getTime(), "yyyy-MM-dd").getTime();
        try {

            long specialDate = longToDate(time, "yyyy-MM-dd").getTime();

            long futureDay = (specialDate - nowDate) / (1000 * 60 * 60 * 24);
            long thisSundayDay = (thisSundayDate - nowDate) / (1000 * 60 * 60 * 24);
            long nextSundayDay = thisSundayDay + 7;

            if (futureDay > nextSundayDay) {
                return futureDay + "天后更新";
            } else if (futureDay > thisSundayDay && futureDay <= nextSundayDay) {
                //下周
                int dayOfWeek = getWeekOfDay(new Date(time));
                return "下周" + getDayForString(dayOfWeek) + "更新";
            } else if (futureDay > 0 && futureDay <= thisSundayDay) {
                // 本周
                int dayOfWeek = getWeekOfDay(new Date(time));
                return "本周" + getDayForString(dayOfWeek) + "更新";
            } else if (futureDay == 0) {
                return "今天更新";
            } else {
                return "等待更新";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "等待更新";
    }

    public static String getDayForString(int day) {
        switch (day) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
            default:
                return "六";
        }
    }

    public static int getWeekOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayWeek;
    }

    public static Date getThisWeekSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static Date getNextWeekSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekSunday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    public static String formatSecond(long second) {
        String timeStr = "00:00";
        if (second > 0) {
            long s = second;
            String format;
            Object[] array;
            Integer hours = (int) (s / HOUR);
            Integer minutes = (int) ((s - hours * HOUR) / MINUTE);
            Integer seconds = (int) (s - hours * HOUR - minutes * MINUTE);
            if (hours > 0) {
                format = "%02d:%02d:%02d";
                array = new Object[]{hours, minutes, seconds};
            } else {
                format = "%02d:%02d";
                array = new Object[]{minutes, seconds};
            }
            timeStr = String.format(format, array);
        }
        return timeStr;
    }
}
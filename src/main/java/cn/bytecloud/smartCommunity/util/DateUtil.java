package cn.bytecloud.smartCommunity.util;

import com.google.common.base.Strings;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    //2.0新加时间结构格式化
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static boolean isValidDateTime(String time) {

        if (Strings.isNullOrEmpty(time)) {
            return false;
        }

        // 指定好一个日期格式的字符串
        String pat = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
        // 指定好正则表达式
        Pattern p = Pattern.compile(pat);
        // 实例化Pattern类
        Matcher m = p.matcher(time);
        // 实例化Matcher类
        if (m.matches()) {
            // 进行验证的匹配，使用正则
            return true;
        } else {
            pat = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}";
            p = Pattern.compile(pat);
            m = p.matcher(time);

            if (m.matches()) {
                return true;
            } else {
                pat = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}";
                p = Pattern.compile(pat);
                m = p.matcher(time);

                if (m.matches()) {
                    return true;
                } else {
                    pat = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
                    p = Pattern.compile(pat);
                    m = p.matcher(time);

                    if (m.matches()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public static boolean isValidTime(String time) {

        if (Strings.isNullOrEmpty(time)) {
            return false;
        }

        // 指定好一个日期格式的字符串
        String pat = "\\d{2}:\\d{2}";
        // 指定好正则表达式
        Pattern p = Pattern.compile(pat);
        // 实例化Pattern类
        Matcher m = p.matcher(time);
        // 实例化Matcher类
        if (m.matches()) {
            // 进行验证的匹配，使用正则
            return true;
        } else {
            pat = "\\d{2}:\\d{2}:\\d{2}";
            p = Pattern.compile(pat);
            m = p.matcher(time);

            if (m.matches()) {
                return true;
            }
        }

        return false;
    }


    public static boolean isValidDate(String date) {

        if (Strings.isNullOrEmpty(date)) {
            return false;
        }

        // 指定好一个日期格式的字符串
        String pat = "\\d{4}-\\d{2}-\\d{2}";
        // 指定好正则表达式
        Pattern p = Pattern.compile(pat);
        // 实例化Pattern类
        Matcher m = p.matcher(date);
        // 实例化Matcher类
        if (m.matches()) {
            // 进行验证的匹配，使用正则
            return true;
        } else {
            pat = "\\d{4}/\\d{2}/\\d{2}";
            p = Pattern.compile(pat);
            m = p.matcher(date);

            if (m.matches()) {
                return true;
            }
        }

        return false;
    }


    public static String toTimeStampFormat(String time) {

        return time.replaceAll("/", "-");

    }


    public static String toDateFormat(String date) {

        return date.replaceAll("/", "-");

    }


    public static int compareDays(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

}

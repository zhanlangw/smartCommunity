package cn.bytecloud.smartCommunity.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号，邮箱正则匹配
 */
public class MatchUtil {

    /**
     * 手机号码正则匹配
     *
     * @author zhanlang
     */
    public static boolean checkTelephone(String telephone) {
        //正则表达式的模式
        String regex = "^1[\\d]{10}";
        //正则表达式的匹配器
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(telephone);
        //进行正则匹配
        return m.matches();
    }

    /**
     * 邮箱正则匹配
     *
     * @author zhanlang
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, email);
    }


    /**
     * 检查密码是否包含数字和英文
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        boolean numFlag = false;
        boolean englishFlag = false;
        for (int i = 0; i < password.length(); i++) {
            int num = (int) password.charAt(i);
            if (!numFlag) {
                if (num <= 57 && num >= 49) {
                    numFlag = true;
                }
            }
            if (!englishFlag) {
                if ((num <= 90 && num >= 65) || (num <= 122 && num >= 97)) {
                    englishFlag = true;
                }
            }
        }
        return (numFlag && englishFlag);
    }
}

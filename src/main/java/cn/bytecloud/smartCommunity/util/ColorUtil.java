package cn.bytecloud.smartCommunity.util;

import java.awt.*;

public class ColorUtil {

    /**
     * Color对象转换成字符串
     *
     * @param color Color对象
     * @return 16进制颜色字符串
     */
    public static String toHexFromColor(Color color) {
        String r, g, b;
        StringBuilder su = new StringBuilder();
        r = Integer.toHexString(color.getRed());
        g = Integer.toHexString(color.getGreen());
        b = Integer.toHexString(color.getBlue());
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("0xFF");
        su.append(r);
        su.append(g);
        su.append(b);
        //0xFF0000FF  
        return su.toString();
    }

    /**
     * 字符串转换成Color对象
     *
     * @param colorStr 16进制颜色字符串 #999999 到Color对象
     * @return Color对象
     */
    public static Color toColorFromString(String colorStr) {
        int i = Integer.parseInt(colorStr.substring(1), 16);
        return new Color(i);
    }
}

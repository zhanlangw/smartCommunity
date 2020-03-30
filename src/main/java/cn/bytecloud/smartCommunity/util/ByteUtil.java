package cn.bytecloud.smartCommunity.util;

public class ByteUtil {

    /**
     * 16进制中的字符集
     */
    private static final String HEX_CHAR = "0123456789ABCDEF";

    /**
     * 16进制中的字符集对应的字节数组
     */
    private static final byte[] HEX_STRING_BYTE = HEX_CHAR.getBytes();

    public static byte[] byteToHex(byte[] b) {
        int length = b.length;
        byte[] b2 = new byte[length << 1];
        int pos;
        for (int i = 0; i < length; i++) {
            pos = 2 * i;
            b2[pos] = HEX_STRING_BYTE[(b[i] & 0xf0) >> 4];
            b2[pos + 1] = HEX_STRING_BYTE[b[i] & 0x0f];
        }
        return b2;
    }

    public static byte[] yearToHexBytes(int year) {

        byte a = (byte) (year / 100);
        byte b = (byte) (year % (a * 100));

        byte[] out = {a, b};
        return out;
    }

    public static String byteToHexString(byte value) {
        StringBuilder stringBuilder = new StringBuilder("");
        int v = value & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);

        return stringBuilder.toString().toUpperCase();
    }

    public static String bytesToHexString(byte[] value) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (value == null || value.length <= 0) {
            return null;
        }
        for (int i = 0; i < value.length; i++) {
            int v = value[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static boolean compareBytes(byte[] one, byte[] two) {

        if (one.length == two.length && one.length > 0) {
            for (int i = 0; i < one.length; i++) {
                if (one[i] != two[i]) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public static char[] bytesToMac(byte[] src) {

        char[] mac = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < src.length; i += 2) {
            int b1 = src[i] & 0x0f;
            int b2 = src[i] & 0xf0 >> 4;

            mac[i] = (char) b1;
            mac[i + 1] = (char) b2;
        }

        return mac;
    }

    public static int bytes2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    public static int byte2int(byte res) {
        return 0xff & res;
    }
}

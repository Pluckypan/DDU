package engineer.echo.easyapi.annotation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 与MD5相关的工具类
 */
class MD5Tool {

    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 获取字符串对应的MD5
     * @param password 字符串
     * @return 字符串对应的MD5
     */
    static String getMD5(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte[] messageDigest = digest.digest();
            return toHexString(messageDigest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将字节数组转换成String
     * @param b 字节数组
     * @return 转换后的String
     */
    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte value : b) {
            sb.append(HEX_DIGITS[(value & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[value & 0x0f]);
        }
        return sb.toString();
    }

}

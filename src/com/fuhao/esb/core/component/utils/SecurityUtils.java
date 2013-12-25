package com.fuhao.esb.core.component.utils;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.util.Calendar;
import java.util.UUID;
/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public final class SecurityUtils {

    // 用来将字节转换成 16 进制表示的字符
    private static final char[] HexDigitsUpper = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    // 用来将字节转换成 16 进制表示的字符
    private static final char[] HexDigitsLover = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final byte[] key = new byte[] { 88, 125, -93, -70, -82, -66, -90, 18, 104, 36, 101, 7, -107, 107, 109, -112, 75, 64,
            -112, -70, -67, 14, 67, 23, -125, 76, -72, 9, -114, -127, 97, -91, -37, 122, -78, 24, 78, -57, 4, 19, -24, 12, -108, -113, 116,
            5, 78, 95, -37, 36, 0, 121, -113, 15, -37, 104, -111, -80, 22, -106, 91, 83, 17, 20, 0, 80, -35, 37, 55, -13, -99, 15, 108, 32,
            -30, 69, 34, -32, 116, 17, 13, 56, -19, -115, -36, -103, -62, 17, 22, -22, -4, 21, -24, 50, -63, -41, 48, -79, 33, -74, -127,
            31, 75, 119, -23, 110, -10, -29, -67, -127, 19, -77, -84, -104, -24, -5, 114, -30, -126, -43, 56, 127, 112, -20, -100, -27, 35,
            74, -73, 43, 45, 111, 84, 90, -47, -89, -40, -27, -112, -88, -103, 89, -90, 0, -92, 104, -35, 54, 119, -52, -6, -74, -29, -89,
            1, -4, 86, -7, 95, -121, 69, -34, 1, 43, 111, 113, -57, 1, -72, -4, 78, 108, -69, 50, -83, -9, -118, -115, 120, 7, -30, -33,
            126, 120, 69, 66, -34, 41, -27, -34, -52, -86, 109, -63, 73, 40, -94, -48, 46, 37, -120, 113, -87, -28, 95, 118, 15, 26, 18, 1,
            -104, -98, -3, -113, -111, 26, 82, -41, -98, -33, -113, -44, 72, 107, 41, -128, -102, -7, 84, -80, -39, 91, 77, 21, 61, -99,
            114, 62, -42, 127, -106, -79, 35, 92, -22, -47, -19, 85, 65, -81, -1, 66, -117, -96, 113, 3 };

    /**
     * 基于口令的加密算法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-9-29下午3:01:38
     * @param head
     * @param passwd
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String encryptByPasESB(String head, String passwd) throws ESBBaseCheckedException {
        byte[] data = ESBSecurityUtils
                .encryptByDES((head + UUID.randomUUID() + passwd).getBytes(), ESBSecurityUtils.initKeyForDES(key));
        return ESBSecurityUtils.encodeBase64(data).replaceAll("\n", "{n}").replaceAll("\r", "{r}");
    }

    /**
     * 基于口令的解密算法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-9-29下午3:02:02
     * @param head
     * @param passwd
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String decryptByPasESB(String head, String passwd) throws ESBBaseCheckedException {
        if (passwd == null || "".equals(passwd)) {
            return null;
        }

        byte[] data = null;
        try {
            passwd = passwd.replaceAll("\\{n\\}", "\n").replaceAll("\\{r\\}", "\r");
            data = ESBSecurityUtils.decryptByDES(ESBSecurityUtils.decodeBase64(passwd.getBytes("UTF-8")),
                    ESBSecurityUtils.initKeyForDES(key));
        } catch (Throwable ex) {
            return passwd;
        }

        String tmp = new String(data);
        if (tmp.startsWith(head)) {
            return tmp.substring(head.length() + UUID.randomUUID().toString().length());
        } else {
            return passwd;
        }
    }

    /**
     * 计算摘要
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param algorithm
     * @param is
     * @param autoClose
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static byte[] digest(String algorithm, InputStream is, boolean autoClose) throws ESBBaseCheckedException {
        // 优化文件流的读取
        if (is instanceof FileInputStream) {
            is = new BufferedInputStream(is);
        }

        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm);

            // 读取字节并更新消息摘要
            DigestInputStream dis = new DigestInputStream(is, md);
            while (dis.read() != -1)
                ;
            return md.digest();
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("UT-09001:计算" + algorithm + "值时发生错误", ex);
        } finally {
            if (autoClose) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("关闭" + algorithm + "的计算参数流时发生错误", ex);
                }
            }
        }
    }

    public static String convertBytesToHexString(byte[] bytes, boolean toLoverCase) {
        char str[] = new char[bytes.length * 2];
        char[] hexDigits = toLoverCase ? HexDigitsLover : HexDigitsUpper;

        for (int i = 0, k = 0; i < bytes.length; i++) {
            // 转换成 16 进制字符的转换
            byte byte0 = bytes[i]; // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }

        return new String(str); // 换后的结果转换为字符串
    }

    /**
     * 计算MD5值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param is
     * @param autoClose
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String md5(InputStream is, boolean autoClose) throws ESBBaseCheckedException {
        return convertBytesToHexString(digest("MD5", is, autoClose), false);
    }

    /**
     * 计算MD5值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String md5(byte[] data) throws ESBBaseCheckedException {
        return md5(new ByteArrayInputStream(data), false);
    }

    /**
     * 计算SHA值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param is
     * @param autoClose
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String sha(InputStream is, boolean autoClose) throws ESBBaseCheckedException {
        return convertBytesToHexString(digest("SHA", is, autoClose), false);
    }

    /**
     * 计算SHA值
     *
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     */
    public static String sha(byte[] data) throws ESBBaseCheckedException {
        return sha(new ByteArrayInputStream(data), false);
    }

    /**
     * 计算SHA-256值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param is
     * @param autoClose
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String sha256(InputStream is, boolean autoClose) throws ESBBaseCheckedException {
        return convertBytesToHexString(digest("SHA-256", is, autoClose), false);
    }

    /**
     * 计算SHA-256值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String sha256(byte[] data) throws ESBBaseCheckedException {
        return sha256(new ByteArrayInputStream(data), false);
    }

    /**
     * 计算SHA-384值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param is
     * @param autoClose
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String sha384(InputStream is, boolean autoClose) throws ESBBaseCheckedException {
        return convertBytesToHexString(digest("SHA-384", is, autoClose), false);
    }

    /**
     * 计算SHA-384值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String sha384(byte[] data) throws ESBBaseCheckedException {
        return sha384(new ByteArrayInputStream(data), false);
    }

    /**
     * 计算SHA-512值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param is
     * @param autoClose
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String sha512(InputStream is, boolean autoClose) throws ESBBaseCheckedException {
        return convertBytesToHexString(digest("SHA-512", is, autoClose), false);
    }

    /**
     * 计算SHA-512值
     *
     * @Time 创建时间:2011-9-23上午10:11:34
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String sha512(byte[] data) throws ESBBaseCheckedException {
        return sha512(new ByteArrayInputStream(data), false);
    }

    /**
     * 基于时间的动态令牌生成算法
     *
     * @Time 创建时间:2012-10-21上午10:11:34
     * @param code
     *            唯一序列
     * @param timeSlice
     *            时间片
     * @param length
     *            动态令牌长度
     * @param useSHA512
     *            true:SHA-512算法,false:SHA-256算法
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String[] dynamicTokenByTime(String code, long timeSlice, int length, boolean useSHA512) throws ESBBaseCheckedException {
        long time = Calendar.getInstance().getTimeInMillis() / 1000;
        long remainingTime = time % timeSlice;
        long countdownTime = timeSlice - remainingTime;

        time = time - remainingTime;
        code = code + time;

        try {
            if (useSHA512) {
                code = sha512(code.getBytes("UTF-8"));
            } else {
                code = sha256(code.getBytes("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
        }

        char[] token = new char[length];
        for (int i = 0, j = (int) (time & 0x7F), skip = ((int) (time & 0xF)) + 1; i < length; i++) {
            j += skip;
            if (j >= code.length()) {
                j = j - code.length();
            }
            token[i] = code.charAt(j);
        }

        return new String[] { new String(token), "" + countdownTime };
    }

}

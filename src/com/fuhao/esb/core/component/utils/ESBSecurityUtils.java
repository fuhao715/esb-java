package com.fuhao.esb.core.component.utils;
import com.fuhao.esb.core.component.utils.org.apache.commons.codec.binary.Base64;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBSecurityUtils {
    private static final ThreadLocal<Random> random = new ThreadLocal<Random>() {
        protected synchronized Random initialValue() {
            return new Random(System.nanoTime());
        }
    };

    /**
     * 获取随机数对象
     *
     * @Description 获取到的随机数对象是与当前线程绑定的
     * @Time 创建时间:2012-12-6下午1:43:26
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Random getRandom() {
        return random.get();
    }

    /**
     * 获取唯一GUID
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-6下午1:41:14
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String randomGUID() throws ESBBaseCheckedException {
        StringBuilder str = new StringBuilder(70);
        str.append(SystemUtils.getNetworkInterfaceInfo()).append(":").append(Thread.currentThread().getName())
                .append(":").append(System.nanoTime()).append(":").append(ESBSecurityUtils.getRandom().nextLong());
        return md5(str.toString().getBytes());
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
        return SecurityUtils.md5(is, autoClose);
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
        return SecurityUtils.md5(data);
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
        return SecurityUtils.sha(is, autoClose);
    }

    /**
     * 计算SHA值
     *
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     */
    public static String sha(byte[] data) throws ESBBaseCheckedException {
        return SecurityUtils.sha(data);
    }

    /**
     * 计算SHA-256值
     *
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     */
    public static String sha256(byte[] data) throws ESBBaseCheckedException {
        return SecurityUtils.sha256(data);
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
        return SecurityUtils.sha256(is, autoClose);
    }

    /**
     * 计算SHA-384值
     *
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     */
    public static String sha384(byte[] data) throws ESBBaseCheckedException {
        return SecurityUtils.sha384(data);
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
        return SecurityUtils.sha384(is, autoClose);
    }

    /**
     * 计算SHA-512值
     *
     * @param data
     * @return
     * @throws ESBBaseCheckedException
     */
    public static String sha512(byte[] data) throws ESBBaseCheckedException {
        return SecurityUtils.sha512(data);
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
        return SecurityUtils.sha512(is, autoClose);
    }

    // ----------------------------------------Base64编/解码算法相关--开始------------------------------------------------------
    /**
     * 对二进制数据进行Base64编码
     *
     * @Time 创建时间:2011-9-23上午10:14:35
     * @param data
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String encodeBase64(byte[] data) {
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 对Base64算法产生的二进制数据进行解码
     *
     * @Time 创建时间:2011-9-23上午10:14:35
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static byte[] decodeBase64(byte[] data) {
        return Base64.decodeBase64(data);
    }

    // ----------------------------------------Base64编/解码算法相关--结束------------------------------------------------------

    // ----------------------------------------DES加密算法相关--开始------------------------------------------------------
    private static final String ALGORITHM_DES = "DES";

    /**
     * 生成密钥
     *
     * @return
     * @throws ESBBaseCheckedException
     */
    public static byte[] initKeyForDES() throws ESBBaseCheckedException {
        return initKeyForDES((byte[]) null);
    }

    public static byte[] initKeyForDES(String pasESB) throws ESBBaseCheckedException {
        return initKeyForDES(pasESB != null ? pasESB.getBytes() : null);
    }

    /**
     * 生成密钥
     *
     * @return
     * @throws ESBBaseCheckedException
     */
    public static byte[] initKeyForDES(byte[] pasESB) throws ESBBaseCheckedException {
        SecureRandom secureRandom = null;

        if (pasESB != null) {
            secureRandom = new SecureRandom(decodeBase64(pasESB));
        } else {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(ALGORITHM_DES);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESBBaseCheckedException("UT-09002:系统不支持DES加密算法", ex);
        }
        kg.init(secureRandom);

        SecretKey secretKey = kg.generateKey();

        return secretKey.getEncoded();
    }

    /**
     * 转换密钥
     *
     * @param key
     * @return
     * @throws ESBBaseCheckedException
     */
    private static Key toKeyForDES(byte[] key) throws ESBBaseCheckedException {
        DESKeySpec dks;
        SecretKeyFactory keyFactory;
        SecretKey secretKey;
        try {
            dks = new DESKeySpec(key);
        } catch (InvalidKeyException ex) {
            throw new ESBBaseCheckedException("UT-09003:生成DES密钥对象时发生错误", ex);
        }

        try {
            keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESBBaseCheckedException("UT-09002:系统不支持DES加密算法", ex);
        }

        try {
            secretKey = keyFactory.generateSecret(dks);
        } catch (InvalidKeySpecException ex) {
            throw new ESBBaseCheckedException("UT-09004:生成安全密钥时发生错误", ex);
        }

        return secretKey;
    }

    /**
     * DES加密
     *
     * @param key
     * @return
     * @throws ESBBaseCheckedException
     */
    public static void encryptByDES(InputStream is, OutputStream os, byte[] key, boolean autoClose)
            throws ESBBaseCheckedException {
        CipherInputStream cis = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.ENCRYPT_MODE, toKeyForDES(key));
            cis = new CipherInputStream(is, cipher);

            byte[] buf = new byte[512];
            int len = -1;
            while ((len = cis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (NoSuchAlgorithmException ex) {
            throw new ESBBaseCheckedException("UT-09002:系统不支持DES加密算法", ex);
        } catch (NoSuchPaddingException ex) {
            throw new ESBBaseCheckedException("UT-09002:系统不支持DES加密算法", ex);
        } catch (InvalidKeyException ex) {
            throw new ESBBaseCheckedException("UT-09005:初始化DES加密器时发生错误", ex);
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-08007:操作加密数据流时发生错误", ex);
        } finally {
            if (autoClose) {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-08008:关闭输出流时发生错误", ex);
                } finally {
                    if (cis != null) {
                        try {
                            cis.close();
                        } catch (IOException ex) {
                            throw new ESBBaseCheckedException("UT-08009:关闭输入流时发生错误", ex);
                        }
                    }
                }
            }

        }
    }

    /**
     * DES加密
     *
     * @param data
     * @param key
     * @return
     * @throws ESBBaseCheckedException
     */
    public static byte[] encryptByDES(byte[] data, byte[] key) throws ESBBaseCheckedException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        encryptByDES(new ByteArrayInputStream(data), baos, key, false);
        return baos.toByteArray();
    }

    /**
     * DES解密
     *
     * @param key
     * @return
     * @throws ESBBaseCheckedException
     */
    public static void decryptByDES(InputStream is, OutputStream os, byte[] key, boolean autoClose)
            throws ESBBaseCheckedException {
        CipherInputStream cis = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.DECRYPT_MODE, toKeyForDES(key));
            cis = new CipherInputStream(is, cipher);

            byte[] buf = new byte[512];
            int len = -1;
            while ((len = cis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (NoSuchAlgorithmException ex) {
            throw new ESBBaseCheckedException("UT-09002:系统不支持DES加密算法", ex);
        } catch (NoSuchPaddingException ex) {
            throw new ESBBaseCheckedException("UT-09002:系统不支持DES加密算法", ex);
        } catch (InvalidKeyException ex) {
            throw new ESBBaseCheckedException("UT-09005:初始化DES加密器时发生错误", ex);
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-08007:操作加密数据流时发生错误", ex);
        } finally {
            if (autoClose) {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-08008:关闭输出流时发生错误", ex);
                } finally {
                    if (cis != null) {
                        try {
                            cis.close();
                        } catch (IOException ex) {
                            throw new ESBBaseCheckedException("UT-08009:关闭输入流时发生错误", ex);
                        }
                    }
                }
            }

        }
    }

    /**
     * DES解密
     *
     * @param data
     * @param key
     * @return
     * @throws ESBBaseCheckedException
     */
    public static byte[] decryptByDES(byte[] data, byte[] key) throws ESBBaseCheckedException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decryptByDES(new ByteArrayInputStream(data), baos, key, false);
        return baos.toByteArray();
    }

    @Deprecated
    public static void DESDemo(String[] args) throws ESBBaseCheckedException, IOException {
        String data = "abcdefg";
        String pasESB = "zjx";

        // 单条加解密
        String str = new String(encodeBase64(encryptByDES(data.getBytes(), initKeyForDES(pasESB))));
        System.out.println(str);
        str = new String((decryptByDES(decodeBase64(str.getBytes()), initKeyForDES(pasESB))));
        System.out.println(str);

        // 批量加解密
        byte[] key = initKeyForDES(pasESB);
        // for(){
        str = new String(encodeBase64(encryptByDES(data.getBytes(), key)));
        System.out.println(str);
        str = new String(decryptByDES(decodeBase64(str.getBytes()), key));
        System.out.println(str);
        // }
    }

    // ----------------------------------------DES加密算法相关--开始------------------------------------------------------

    // ----------------------------------------PBE加密算法相关--开始------------------------------------------------------
    /**
     * PBE加密算法
     */
    private static final String ALGORITHM_PBE = "PBEWITHMD5andDES";

    /**
     * 随机盐初始化
     *
     * @return
     * @throws Exception
     */
    public static byte[] initSalt() throws Exception {
        byte[] salt = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    /**
     * 转换密钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKeyForPBE(char[] key) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_PBE);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return secretKey;
    }

    /**
     * PBE加密算法
     *
     * @param is
     *            输入
     * @param os
     *            输出
     * @param pwd
     *            密码
     * @param salt
     *            随机盐
     * @param autoClose
     *            自动关闭输入输出流
     * @throws Exception
     */
    public static void encryptByPBE(InputStream is, OutputStream os, String pwd, byte[] salt, boolean autoClose)
            throws Exception {
        char[] pasESB = pwd.toCharArray();
        CipherInputStream cis = null;

        try {
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
            Cipher cipher = Cipher.getInstance(ALGORITHM_PBE);
            cipher.init(Cipher.ENCRYPT_MODE, toKeyForPBE(pasESB), paramSpec);
            cis = new CipherInputStream(is, cipher);

            byte[] buf = new byte[512];
            int len = -1;
            while ((len = cis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (NoSuchAlgorithmException ex) {
            throw new ESBBaseCheckedException("UT-09006:系统不支持PBE加密算法", ex);
        } catch (NoSuchPaddingException ex) {
            throw new ESBBaseCheckedException("UT-09006:系统不支持PBE加密算法", ex);
        } catch (InvalidKeyException ex) {
            throw new ESBBaseCheckedException("UT-09007:初始化PBE加密器时发生错误", ex);
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-08007:操作加密数据流时发生错误", ex);
        } finally {
            if (autoClose) {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-08008:关闭输出流时发生错误", ex);
                } finally {
                    if (cis != null) {
                        try {
                            cis.close();
                        } catch (IOException ex) {
                            throw new ESBBaseCheckedException("UT-08009:关闭输入流时发生错误", ex);
                        }
                    }
                }
            }

        }
    }

    /**
     * PBE加密算法
     *
     * @param data
     *            数据
     * @param pwd
     *            密码
     * @param salt
     *            随机盐
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPBE(byte[] data, String pwd, byte[] salt) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        encryptByPBE(new ByteArrayInputStream(data), baos, pwd, salt, false);
        return baos.toByteArray();

    }

    /**
     * PBE解密算法
     * @param pwd
     *            密码
     * @param salt
     *            随机盐
     * @throws Exception
     */
    public static void decryptByPBE(InputStream is, OutputStream os, String pwd, byte[] salt, boolean autoClose)
            throws Exception {
        char[] pasESB = pwd.toCharArray();
        CipherInputStream cis = null;

        try {
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
            Cipher cipher = Cipher.getInstance(ALGORITHM_PBE);
            cipher.init(Cipher.DECRYPT_MODE, toKeyForPBE(pasESB), paramSpec);
            cis = new CipherInputStream(is, cipher);

            byte[] buf = new byte[512];
            int len = -1;
            while ((len = cis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (NoSuchAlgorithmException ex) {
            throw new ESBBaseCheckedException("UT-09006:系统不支持PBE加密算法", ex);
        } catch (NoSuchPaddingException ex) {
            throw new ESBBaseCheckedException("UT-09006:系统不支持PBE加密算法", ex);
        } catch (InvalidKeyException ex) {
            throw new ESBBaseCheckedException("UT-09007:初始化PBE加密器时发生错误", ex);
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-08007:操作加密数据流时发生错误", ex);
        } finally {
            if (autoClose) {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("关闭输出流时发生错误", ex);
                } finally {
                    if (cis != null) {
                        try {
                            cis.close();
                        } catch (IOException ex) {
                            throw new ESBBaseCheckedException("关闭输入流时发生错误", ex);
                        }
                    }
                }
            }

        }
    }

    /**
     * PBE解密算法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-3-9上午9:52:47
     * @param data
     * @param pwd
     * @param salt
     * @return
     * @throws Exception
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static byte[] decryptByPBE(byte[] data, String pwd, byte[] salt) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decryptByPBE(new ByteArrayInputStream(data), baos, pwd, salt, false);
        return baos.toByteArray();
    }

    @Deprecated
    public static void PBEDemo(String[] args) throws Exception {
        String inputStr = "abc";
        System.err.println("原文: " + inputStr);
        byte[] input = inputStr.getBytes();

        String pwd = "efg";
        System.err.println("密码: " + pwd);

        byte[] salt = initSalt();

        byte[] data = encryptByPBE(input, pwd, salt);

        System.err.println("加密后: " + new String(encodeBase64(data)));

        byte[] output = decryptByPBE(data, pwd, salt);
        String outputStr = new String(output);

        System.err.println("解密后: " + outputStr);
    }

    // ----------------------------------------PBE加密算法相关--结束------------------------------------------------------

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
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String[] dynamicTokenByTime(String code, long timeSlice, int length, boolean useSHA512)
            throws ESBBaseCheckedException {
        return SecurityUtils.dynamicTokenByTime(code, timeSlice, length, useSHA512);
    }

    @Deprecated
    public static void DTTDemo(String[] args) throws InterruptedException, ESBBaseCheckedException {
        while (true) {
            String[] token = dynamicTokenByTime("000000000000000", 30, 9, true);
            System.out
                    .println(ESBDateUtils.toDateTimeStr(Calendar.getInstance()) + "--" + token[0] + "--" + token[1]);
            Thread.sleep(1000);
        }
    }
}

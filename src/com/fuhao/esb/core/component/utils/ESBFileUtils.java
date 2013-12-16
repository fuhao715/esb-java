package com.fuhao.esb.core.component.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import org.xml.sax.SAXException;
/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBFileUtils {


    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBFileUtils.class);

    private static String ESB_FILE = null;

    /**
     * 获取目录下的所有文件
     *
     * @Description 相关说明
     * @Time 创建时间:2012-3-30下午12:07:26
     * @param directory
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static LinkedList<String> listFiles(String directory) throws ESBBaseCheckedException {
        LinkedList<String> files = new LinkedList<String>();
        File dir = new File(directory);

        if (!dir.exists()) {
            throw new ESBBaseCheckedException("UT-06001:目录" + directory + "不存在");
        }
        if (!dir.isDirectory()) {
            throw new ESBBaseCheckedException("UT-06002:" + directory + "不是目录");
        }

        findSubFile(new File(directory), files);
        files.removeFirst();
        return files;
    }

    private static void findSubFile(File f, LinkedList<String> files) throws ESBBaseCheckedException {
        if (!f.exists()) {
            throw new ESBBaseCheckedException("UT-06003:文件或目录" + f + "不存在或已经删除");
        }

        if (f.isFile()) {
            files.add(f.getAbsolutePath());
        } else if (f.isDirectory()) {
            files.add(f.getAbsolutePath());
            for (File subFile : f.listFiles()) {
                findSubFile(subFile, files);
            }
        } else {
            // 获取其它类型的对象
        }
    }

    /**
     * 读取资源
     *
     * @Description 以输入流的形式读取资源
     * @Time 创建时间:2011-9-1上午9:46:27
     * @param name
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static InputStream getInputStream(String name, Class<?> clazz) {
        InputStream is = null;
        if (clazz != null) {
            name = "/" + clazz.getPackage().getName().replace('.', '/') + "/" + name;
            is = clazz.getResourceAsStream(name);
            if (is == null) {
                is = clazz.getClassLoader().getResourceAsStream(name);
                printFile(name, null, clazz.getClassLoader());
            } else {
                printFile(name, clazz, null);
            }
        } else {
            is = ESBFileUtils.class.getResourceAsStream(name);
            if (is == null) {
                is = ESBFileUtils.class.getClassLoader().getResourceAsStream(name);
                if (is == null) {
                    try {
                        is = new FileInputStream(name);
                    } catch (FileNotFoundException ex) {
                        is = null;
                    }
                }
                printFile(name, null, ESBFileUtils.class.getClassLoader());
            } else {
                printFile(name, ESBFileUtils.class, null);
            }
        }
        return is;
    }

    private static void printFile(String file, Class<?> clazz, ClassLoader loader) {
        URL url = null;
        if (clazz != null) {
            url = clazz.getResource(file);
        } else {
            url = loader.getResource(file);
        }
        if (url != null) {
            log.debug("开始读取文件" + url);
        }
    }

    /**
     * 读取资源
     *
     * @Description 以输入流读取器的形式读取资源
     * @Time 创建时间:2011-8-29下午4:39:51
     * @param name
     * @para clazz
     * @param charsetName
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static InputStreamReader getInputStreamReader(String name, Class<?> clazz, String charsetName) throws ESBBaseCheckedException {
        InputStreamReader reader = null;
        try {
            if (charsetName == null) {
                reader = new InputStreamReader(getInputStream(name, clazz));
            } else {
                reader = new InputStreamReader(getInputStream(name, clazz), charsetName);
            }
        } catch (UnsupportedEncodingException ex) {
            throw new ESBBaseCheckedException("UT-06004:不支持的字符集类型" + charsetName, ex);
        }

        return reader;
    }

    /**
     * 获取资源URL
     *
     * @Description 获取资源URL
     * @Time 创建时间:2011-8-29下午4:35:40
     * @param name
     * @param clazz
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static URL getResource(String name, Class<?> clazz) throws ESBBaseCheckedException {
        URL url = null;
        if (clazz != null) {
            url = clazz.getResource(name);
        } else {
            url = ESBFileUtils.class.getResource(name);
        }

        if (url == null) {
            try {
                File f = new File(name);
                if (f.exists()) {
                    url = f.toURI().toURL();
                }
            } catch (MalformedURLException ex) {
                throw new ESBBaseCheckedException("生成文件" + name + "的URL对象时发生错误", ex);
            }
        }

        return url;
    }

    /**
     * 读取资源URL
     *
     * @Description 读取资源URL
     * @Time 创建时间:2011-9-1上午10:18:48
     * @param name
     * @return
     * @throws IOException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Enumeration<URL> getResources(String name) throws IOException {
        return ESBFileUtils.class.getClassLoader().getResources(name);
    }

    public static void setESBRootPath(String path) throws ESBBaseCheckedException {
        if (ESB_FILE != null) {
            log.debug("根目录已经设置为", ESB_FILE, "，不允许修改为", path);
            return;
        }

        URL url = getResource(path, null);

        if (url == null) {
            throw new ESBBaseCheckedException("设置根目录时发生错误，文件" + path + "不存在");
        }

        try {
            ESB_FILE = URLDecoder.decode(url.getPath(), "UTF-8");// 解决路径中含空格、中文问题
        } catch (UnsupportedEncodingException ex) {
            throw new ESBBaseCheckedException("系统不支持的字符集类型UTF-8", ex);
        }

        File root = new File(ESB_FILE);

        if (!root.exists()) {
            throw new ESBBaseCheckedException("根目录或根文件不存在");
        }

        if (root.isFile()) {
            ESB_FILE = root.getParent();
        }

        if (ESB_FILE.endsWith("/") || ESB_FILE.endsWith("\\")) {
            ESB_FILE = ESB_FILE.substring(0, ESB_FILE.length() - 1);
        }
    }

    /**
     * 获取ESB的工作根路径
     *
     * @return
     * @throws ESBBaseCheckedException
     */
    public static String getESBRootPath() throws ESBBaseCheckedException {
        if (ESB_FILE != null) {
            return ESB_FILE;
        }

        setESBRootPath("/ESB.xml");

        return ESB_FILE;
    }

    /**
     * 获取临时文件目录
     *
     * @Time 创建时间:2011-9-27上午9:26:08
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String getTempFileDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取用户当前目录
     *
     * @Time 创建时间:2011-9-27上午9:26:08
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    /**
     * 读取文本文件数据
     *
     * @Description 相关说明
     * @param rs
     * @param charsetName
     * @return
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013-4-3上午10:42:50
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String loadTxtFile(InputStream rs, String charsetName) throws ESBBaseCheckedException {
        StringBuilder str = new StringBuilder();
        BufferedReader reader = null;
        String code = null;

        try {
            reader = new BufferedReader(new InputStreamReader(rs, charsetName));
            while ((code = reader.readLine()) != null) {
                str.append(code).append("\n");
            }
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-06006:读取文件时发生错误", ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                throw new ESBBaseCheckedException("UT-06007:关闭文件时发生错误", ex);
            }
        }

        return str.toString();
    }

    /**
     * 读取文本文件数据
     *
     * @Description 当文本数据文件较小时可使用此方法一次性将文件读入到内存中；<br>
     *              如果文件较大，此方法则不适合，有可能造成内存溢出，同时处理较率不高，应当直接使用下面的方法进行处理:
     *
     *              <pre>
     *              BufferedReader reader = new BufferedReader(readFile(...));
     *              String str = null;
     *              try{
     *              while((str = reader.readLine)!=null){
     *              	......
     *              }catch(IOException ex){
     *              	throw new ESBBaseCheckedException("......",ex);
     *              }finally{
     *              	if(reader != null){
     *              		reader.close();
     *              	}
     *              }
     * </pre>
     *
     *              自行处理文件内容读取和流关闭
     * @Time 创建时间:2011-9-1上午11:27:42
     * @param name
     * @param clazz
     * @param charsetName
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String loadTxtFile(String name, Class<?> clazz, String charsetName) throws ESBBaseCheckedException {
        StringBuilder str = new StringBuilder();
        BufferedReader reader = null;
        String code = null;

        try {
            reader = new BufferedReader(readFile(name, clazz, charsetName));
            while ((code = reader.readLine()) != null) {
                str.append(code).append("\n");
            }
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-06006:读取文件" + name + "时发生错误", ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                throw new ESBBaseCheckedException("UT-06007:关闭文件" + name + "时发生错误", ex);
            }
        }

        return str.toString();
    }

    /**
     * 使用BufferedReader读取文本文件
     *
     * @Description 使用BufferedReader读取文本文件
     * @Time 创建时间:2011-11-2下午1:32:55
     * @param name
     * @param clazz
     * @param charsetName
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static InputStreamReader readFile(String name, Class<?> clazz, String charsetName) throws ESBBaseCheckedException {
        final InputStream is = getInputStream(name, clazz);

        if (is == null) {
            throw new ESBBaseCheckedException("文件" + name + "不存在或无法读取");
        }

        InputStreamReader reader = null;
        try {
            if (charsetName != null) {
                reader = new InputStreamReader(is, charsetName);
            } else {
                reader = new InputStreamReader(is);
            }
        } catch (UnsupportedEncodingException ex) {
            throw new ESBBaseCheckedException("UT-06004:不支持的字符集" + charsetName, ex);
        }

        return reader;
    }

    /**
     * 读取资源文件
     *
     * @Description 读取资源文件
     * @Time 创建时间:2011-9-3下午1:35:11
     * @param name
     * @param clazz
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static byte[] loadFile(String name, Class<?> clazz) throws ESBBaseCheckedException {
        return loadFromStream(getInputStream(name, clazz));
    }

    /**
     * 读取资源流
     *
     * @Description 读取资源流
     * @Time 创建时间:2011-9-3下午1:35:11
     * @param input
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static byte[] loadFromStream(InputStream input) throws ESBBaseCheckedException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int length = -1;
        ESBBaseCheckedException e = null;
        try {
            while ((length = input.read(buf)) != -1) {
                baos.write(buf, 0, length);
            }
            buf = baos.toByteArray();
        } catch (IOException ex) {
            e = new ESBBaseCheckedException("UT-06008:读取文件或写入字节流时发生错误", ex);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                e = (e == null ? new ESBBaseCheckedException("UT-06007:关闭文件时发生错误", ex) : e);
            }
        }

        if (e != null) {
            throw e;
        }
        return buf;
    }

    /**
     * 将数据写入指定的文件中
     *
     * @Time 创建时间:2011-9-29下午10:07:23
     * @param data
     * @param file
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void writeToFile(byte[] data, String file) throws ESBBaseCheckedException {
        writeToFile(new ByteArrayInputStream(data), file, true);
    }

    /**
     * 将读取流中的数据写入指定的文件中
     *
     * @Description 相关说明
     * @Time 创建时间:2012-2-17下午2:02:30
     * @param is
     * @param file
     * @param autoCloseInputStream
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void writeToFile(InputStream is, String file, boolean autoCloseInputStream) throws ESBBaseCheckedException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            throw new ESBBaseCheckedException("UT-06006:打开文件输入流时发生错误", ex);
        }

        byte[] buf = new byte[8192];
        int len = -1;

        try {
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-06008:向文件" + file + "写入数据时发生错误", ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-06007:关闭文件" + file + "输出流时发生错误", ex);
                } finally {
                    if (autoCloseInputStream) {
                        try {
                            is.close();
                        } catch (IOException ex) {
                            throw new ESBBaseCheckedException("UT-06009:关闭数据读取流时发生错误", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 从输入流中读取所有数据到内存中
     *
     * @Description 因为将文件中的数据全部读取到内存中，所以此方法不适宜读取大文件，有可能造成内存溢出
     * @Time 创建时间:2011-9-29下午11:52:04
     * @param inputStream
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static byte[] readAllDataFromInputStreamToMemory(InputStream inputStream) throws ESBBaseCheckedException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;

        try {
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            buffer = baos.toByteArray();
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-06010:从输入流中读取信息时发生异常", ex);
        }

        return buffer;
    }

    /**
     * 将Java对象序列化成文件
     *
     * @Description 相关说明
     * @Time 创建时间:2012-4-9下午5:12:27
     * @param obj
     * @param file
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void saveObjectToFile(Object obj, String file) throws ESBBaseCheckedException {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            oos.writeObject(obj);
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("UT-06011:序列化对象到文件" + file + "时发生错误", ex);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                    oos = null;
                }
            } catch (IOException ex) {
                throw new ESBBaseCheckedException("UT-06007:关闭文件" + file + "时发生错误", ex);
            }
        }
    }

    /**
     * 从文件中反序列化Java对象
     *
     * @Description 相关说明
     * @Time 创建时间:2012-4-9下午5:12:20
     * @param file
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Object readObjectFromFile(String file) throws ESBBaseCheckedException {
        ObjectInputStream ois = null;
        Object obj = null;

        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            obj = ois.readObject();
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("UT-06011:反序列化文件" + file + "中的对象时发生错误", ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    ois = null;
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-06007:关闭文件" + file + "时发生错误", ex);
                }
            }
        }

        return obj;
    }

    /**
     * 使用XSD校验XML格式的正确性
     *
     * @Description 相关说明
     * @Time 创建时间:2012-10-22下午1:05:40
     * @param xml
     *            XML文本
     * @param clazz
     *            与XSD文件处理于同一目录的类
     * @param xsd
     *            XSD文件路径
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void validateXMLByXSD(String xml, Class<?> clazz, String xsd) throws ESBBaseCheckedException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = null;
        try {
            schema = schemaFactory.newSchema(getResource(xsd, clazz));
        } catch (SAXException ex) {
            throw new ESBBaseCheckedException("UT-06012:生成XSD文件" + xsd + "的Schema对象时发生错误", ex);
        }
        try {
            schema.newValidator().validate(new StreamSource(new StringReader(xml)));
        } catch (IOException ex) {
        } catch (SAXException ex) {
            throw new ESBBaseCheckedException("UT-06013:XML文本不符合XSD文件" + xsd + "定义的规范", ex);
        }
    }
}

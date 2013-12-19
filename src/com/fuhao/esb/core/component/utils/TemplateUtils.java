package com.fuhao.esb.core.component.utils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-19.
 * Project Name esb-java
 */
public class TemplateUtils {


    /**
     * 自定义模板缓存
     */
    private static final Map<String, TemplateInfo> templateCache = new LinkedHashMap<String, TemplateInfo>();

    private static class TemplateInfo {
        public String[] template;
        public boolean[] isTemplate;

        public TemplateInfo(int count) {
            template = new String[count];
            isTemplate = new boolean[count];
        }
    }

    /**
     * 解析模板字符串的信息
     *
     * @param template
     * @return
     */
    private static TemplateInfo parseTemplateInfo(String template) {
        TemplateInfo info;
        Pattern p = Pattern.compile("\\$\\w+\\$");
        Matcher m = p.matcher(template);
        int start = 0;
        int end = 0;
        List<Integer> index = new ArrayList<Integer>();
        List<Boolean> isTemplate = new ArrayList<Boolean>();
        while (m.find(start)) {
            if (m.start() != start) {
                isTemplate.add(false);
                index.add(start);
                index.add(m.start());
            }
            isTemplate.add(true);
            index.add(m.start() + 1);
            index.add(end = m.end() - 1);
            start = m.end();
        }
        if (end != template.length()) {
            isTemplate.add(false);
            index.add(end == 0 ? end : end + 1);
            index.add(template.length());
        }

        info = new TemplateInfo(isTemplate.size());
        for (int i = 0, j = 0; i < isTemplate.size(); i++, j += 2) {
            info.template[i] = template.substring(index.get(j), index.get(j + 1));
            info.isTemplate[i] = isTemplate.get(i);
        }
        return info;
    }

    /**
     * 根据模板生成字符串
     */
    public static String generateString(String template, final Map<String, Object> attributes) {
        StringBuilder builder = new StringBuilder();
        TemplateInfo info = templateCache.get(template);

        if (info == null) {
            // 缓存解析的模板信息
            synchronized (template) {
                info = templateCache.get(template);
                if (info == null) {
                    // 解析模板字符串的信息
                    info = parseTemplateInfo(template);
                    // 索引解析得到的模板信息
                    templateCache.put(template, info);
                }
            }
        }

        for (int i = 0; i < info.isTemplate.length; i++) {
            if (info.isTemplate[i]) {
                Object v = attributes.get(info.template[i]);
                builder.append(v == null ? "$" + info.template[i] + "$" : v);
            } else {
                builder.append(info.template[i]);
            }
        }

        return builder.toString();
    }
}

package com.eazybuilder.ci.util;

import cn.hutool.core.util.RandomUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class FreemakerUtils {
    
    /**
     * 使用给定的数据模型和模板内容生成内容
     * @param model
     * @param templateStr
     * @return
     */

    public static String generateByModelAndTemplate(Map<String, Object> model, String templateStr) {
        Configuration config=new Configuration();
        StringReader stringReader=new StringReader(templateStr);
        Template template= null;
        String generateString=null;
        try {
            template = new Template(RandomUtil.randomString(5),stringReader, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            generateString= FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return generateString;
    }
}

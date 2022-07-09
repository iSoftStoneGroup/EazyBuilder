package com.eazybuilder.ci.ant.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUtils {

	public static String processTemplate(Template template,Map<String,Object> model) throws TemplateException, IOException{
		StringWriter sw=new StringWriter();
		template.process(model, sw);
		return sw.toString();
	}
}

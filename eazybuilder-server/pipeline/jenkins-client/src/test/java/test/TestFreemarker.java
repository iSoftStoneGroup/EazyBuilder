package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.entity.BuildJob;
import com.eazybuilder.ci.service.async.PipelineExecuteResult;
import com.eazybuilder.ci.util.JsonMapper;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class TestFreemarker {

    @Test
    public void doConvert() throws Exception{
        Configuration config=new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setTemplateLoader(new FileTemplateLoader(new File("config/template")));
        Template template=config.getTemplate("mail-batch.ftl");

        JsonMapper mapper=JsonMapper.allFieldMapper();
        Map<String,Object> model=Maps.newHashMap();
        model.put("buildUrl", "123");
        Map<String,BuildJob> jobInfoMap=mapper.fromJson(FileUtils.readFileToString(new File("model.json"), "utf-8"),
                mapper.contructMapType(HashMap.class, String.class, BuildJob.class));
        Map<String,List<PipelineExecuteResult>> resultsInfoMap=mapper.fromJson(FileUtils.readFileToString(new File("results.json"), "utf-8"),
                mapper.contructMapType(HashMap.class, String.class, List.class));
        model.putAll(jobInfoMap);
        model.putAll(resultsInfoMap);
        System.out.println(FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
    }

    @Test
    public void doTestStartWith() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
        Configuration config=new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        StringTemplateLoader templateLoader=new StringTemplateLoader();
        templateLoader.putTemplate("test", "<#if 'build'?starts_with('ant')>aaa</#if>bbb");

        config.setTemplateLoader(templateLoader);

        Template template=config.getTemplate("test");

        System.out.println(FreeMarkerTemplateUtils.processTemplateIntoString(template, Maps.newHashMap()));
    }
}

package com.eazybuilder.ci.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.eazybuilder.ci.entity.Project;

public class ConfigUtils {

	public static Project readConfig(File yamlfile) throws IOException{
		try(FileReader fr=new FileReader(yamlfile)){
			YamlReader reader = new YamlReader(fr);
			return reader.read(Project.class);
		}
	}
}

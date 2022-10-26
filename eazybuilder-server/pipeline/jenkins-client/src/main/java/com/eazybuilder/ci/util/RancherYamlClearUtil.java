package com.eazybuilder.ci.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class RancherYamlClearUtil {
	static final String[] RANCHER_FIELDS = { "annotations", "creationTimestamp", "finalizers", "managedFields",
			"selfLink", "ownerReferences" };
	private static Logger logger = LoggerFactory.getLogger(RancherYamlClearUtil.class);

	static final String[] LABLE_FIELDS = { "workload.user.cattle.io/workloadselector" };

	@Test
	public void doClean() {
		clean("D:\\develop\yaml");
	}

	public static void clean(String home) {
		FileUtils.listFiles(new File(home), new String[] { "yaml" }, true).forEach(file -> {
			try {
				clearRancherMetadata(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private static void clearRancherMetadata(File file) throws IOException {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		Yaml yml = new Yaml(options);
		Map dataMap = null;
		logger.info("fileName:{}", file.getName());
		try (FileInputStream fis = new FileInputStream(file)) {
			dataMap = (Map) yml.load(fis);
		}
		String kind = (String) dataMap.get("kind");
		logger.info("kind is:{}", kind);
		if (kind.equals("Ingress") || kind.equals("StorageClass")) {
			return;
		}

		Map metadata = (Map) dataMap.get("metadata");
		String appName = "";
		if (metadata != null) {
			appName = (String) metadata.get("name");
			logger.info("appname is:{}", appName);
			if (metadata.containsKey("labels")) {

				Map<String, String> lables = (Map) metadata.get("labels");
				lables.put("app", appName);
				for (String key : LABLE_FIELDS) {
					lables.remove(key);
				}
			}
			for (String key : RANCHER_FIELDS) {
				metadata.remove(key);
			}
		}
		if (dataMap.containsKey("spec")) {
			Map spec = (Map) dataMap.get("spec");

			if (spec.containsKey("template")) {
				Map template = (Map) spec.get("template");

				if (template.containsKey("spec")) {
					Map specinner = (Map) template.get("spec");
					if (specinner.containsKey("containers")) {
						ArrayList<Map> containers = (ArrayList) specinner.get("containers");
						for (int i = 0; i < containers.size(); i++) {
							
							Map container=containers.get(i);
 
							if (container.containsKey("resources")) {
								container.remove("resources");
							}
						}
					}
				}

				metadata = (Map) template.get("metadata");
				if (metadata != null) {
					if (metadata.containsKey("labels")) {
						Map<String, String> lables = (Map) metadata.get("labels");
						lables.put("app", appName);
						for (String key : LABLE_FIELDS) {
							lables.remove(key);
						}
					}
					for (String key : RANCHER_FIELDS) {
						metadata.remove(key);
					}
				}
			}

			if (spec.containsKey("selector")) {
				Map selector = (Map) spec.get("selector");

				for (String key : LABLE_FIELDS) {
					selector.remove(key);
				}

				if (kind.equals("Service")) {
					selector.put("app", appName);
				}

				if (selector.containsKey("matchLabels")) {
					Map matchLabels = (Map) selector.get("matchLabels");
					matchLabels.put("app", appName);
					for (String key : LABLE_FIELDS) {
						matchLabels.remove(key);
					}

				}

			}

		}

		try (FileWriter writer = new FileWriter(file)) {
			yml.dump(dataMap, writer);
		}
	}
}

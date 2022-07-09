package com.eazybuilder.ci.storage;

import java.io.File;
import java.io.IOException;

import com.eazybuilder.ci.entity.report.ResourceItem;

public interface ResourceStorageService {
	
	public String save(ResourceItem item) throws IOException;
	
	public String save(File file) throws IOException;
	
	public ResourceItem get(String id) throws IOException;
}

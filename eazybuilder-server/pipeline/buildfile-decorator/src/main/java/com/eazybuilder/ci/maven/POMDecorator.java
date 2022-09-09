package com.eazybuilder.ci.maven;

import org.apache.maven.model.Model;

public interface POMDecorator {

	public void decorate(Model original,String nexusUrl);
}

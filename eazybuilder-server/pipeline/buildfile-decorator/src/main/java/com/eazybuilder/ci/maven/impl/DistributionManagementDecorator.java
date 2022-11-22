package com.eazybuilder.ci.maven.impl;

import com.eazybuilder.ci.maven.POMDecorator;
import org.apache.maven.model.DeploymentRepository;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Model;

public class DistributionManagementDecorator implements POMDecorator {
//	private static final DeploymentRepository REPOSITORY=new DeploymentRepository();
//	private static final DeploymentRepository SNAPSHOT_REPOSITORY=new DeploymentRepository();
//	private static Properties properties = new LoadConfigYML().getConfigProperties();



	//	static{
//		REPOSITORY.setId("Eazybuilder-releases");
//		REPOSITORY.setUrl("http://nexus3.eazybuilder-devops.cn/repository/devops_repo");
//
//		SNAPSHOT_REPOSITORY.setId("Eazybuilder-snapshots");
//		SNAPSHOT_REPOSITORY.setUrl("http://nexus3.eazybuilder-devops.cn/repository/devops_repo");
//	}
	@Override
	public void decorate(Model original,String nexusUrl) {
		if(original.getDistributionManagement()!=null){
			return;
		}

		DeploymentRepository repository=new DeploymentRepository();
		repository.setId("Eazybuilder-releases");
		repository.setUrl(nexusUrl);
		DistributionManagement distributionManagement= new DistributionManagement();
		distributionManagement.setRepository(repository);
		DeploymentRepository snapshotRepository=new DeploymentRepository();
		snapshotRepository.setId("Eazybuilder-snapshots");
		snapshotRepository.setUrl(nexusUrl);
		distributionManagement.setSnapshotRepository(snapshotRepository);
		original.setDistributionManagement(distributionManagement);
	}

}

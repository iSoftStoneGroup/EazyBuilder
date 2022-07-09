package com.eazybuilder.ci.service;

import java.util.Collection;
import java.util.List;

import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.repository.DockerRegistryDao;
import com.eazybuilder.ci.util.AuthUtils;
@Service
public class DockerRegistryService extends AbstractCommonServiceImpl<DockerRegistryDao, DockerRegistry> 
				implements CommonService<DockerRegistry>{

	@Autowired
    TeamServiceImpl teamServiceImpl;
	
	
	public List<DockerRegistry> findByUrl(String url) {
		return (List<DockerRegistry>)dao.findAll(QDockerRegistry.dockerRegistry.url.eq(url));

	}

	public DockerRegistry findByUrlAndTeamId(String url,String teamId) {
		return dao.findOne(QDockerRegistry.dockerRegistry.url.eq(url).and(QDockerRegistry.dockerRegistry.teamId.eq(teamId))).get();
	}

	@Override
	public void save(DockerRegistry entity) {
		if(entity.getId()!=null&&!entity.isChangePwd()) {
			DockerRegistry dbEntity=dao.findById(entity.getId()).get();
			entity.setPassword(dbEntity.getPassword());
		}
		super.save(entity);
	}

	@Override
	public Page<DockerRegistry> pageSearch(Pageable pageable, String searchText) {
		if(Role.existRole(AuthUtils.getCurrentUser().getRoles(),RoleEnum.admin)) {
			return super.pageSearch(pageable, searchText);
		}
		return dao.findAll(
				QDockerRegistry.dockerRegistry
				.teamId.in(teamServiceImpl.getMyTeamIds()), pageable);
	}
	
	public Collection<DockerRegistry> findByUser(User user){
		return (Collection<DockerRegistry>) dao.findAll(QDockerRegistry.dockerRegistry.teamId.in(teamServiceImpl.getMyTeamIds()));
	}

	@Override
	public Iterable<DockerRegistry> findAll() {
		User currentUser=AuthUtils.getCurrentUser();
		if(currentUser!=null
				&& !Role.existRole(AuthUtils.getCurrentUser().getRoles(),RoleEnum.admin)){
			//filted by current user team
			return findByUser(currentUser);
		}
		return dao.findAll();
	}
	
}

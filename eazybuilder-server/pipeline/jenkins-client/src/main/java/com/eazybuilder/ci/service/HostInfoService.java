package com.eazybuilder.ci.service;

import java.util.Collection;

import com.eazybuilder.ci.constant.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.HostInfo;
import com.eazybuilder.ci.entity.QHostInfo;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.repository.HostInfoDao;
import com.eazybuilder.ci.util.AuthUtils;

@Service
public class HostInfoService extends AbstractCommonServiceImpl<HostInfoDao, HostInfo> 
		implements CommonService<HostInfo>{

	@Autowired
    TeamServiceImpl teamServiceImpl;
	
	@Override
	public Page<HostInfo> pageSearch(Pageable pageable, String searchText) {
		if(Role.existRole(AuthUtils.getCurrentUser().getRoles(), RoleEnum.admin)) {
			return super.pageSearch(pageable, searchText);
		}
		return dao.findAll(QHostInfo.hostInfo.teamId.in(teamServiceImpl.getMyTeamIds()), pageable);
	}

	@Override
	public Iterable<HostInfo> findAll() {
		User currentUser=AuthUtils.getCurrentUser();
		if(currentUser!=null
				&&Role.existRole(AuthUtils.getCurrentUser().getRoles(), RoleEnum.admin)){
			//filted by current user team
			return findByUser(currentUser);
		}
		return dao.findAll();
	}

	private Iterable<HostInfo> findByUser(User currentUser) {
		return (Collection<HostInfo>) dao.findAll(QHostInfo.hostInfo.teamId.in(teamServiceImpl.getMyTeamIds()));
		
	}
	
}

package com.eazybuilder.ci.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.base.PageSearch;
import com.eazybuilder.ci.entity.QUser;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.repository.UserDao;
import com.eazybuilder.ci.util.AuthUtils;

import java.util.List;

@PageSearch(value = QUser.class, fields = {"name"})
@Service
public class UserService extends AbstractCommonServiceImpl<UserDao, User> implements CommonService<User>{

	public static final String DEFAULT_TOKEN="Ci#123456";
	
	public User findByEmail(String email){
		return dao.findOne(QUser.user.email.eq(email)).orElse(null);
	}
	public List<User> findByUserName(String userName){
		return (List<User>)dao.findAll(QUser.user.name.eq(userName));
	}
	public void save(User entity){
		save(entity,false);
	}
	
	public void save(User entity,boolean modifyPwd){
		if(StringUtils.isBlank(entity.getId())){
			try {
				entity.setPassword(AuthUtils.encrypt(modifyPwd?entity.getPassword():UserService.DEFAULT_TOKEN, entity.getEmail()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			User user=findOne(entity.getId());
			if(modifyPwd){
				BeanUtils.copyProperties(user, entity, "name","role","phone","password");
				try {
					entity.setPassword(AuthUtils.encrypt(entity.getPassword(), entity.getEmail()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				BeanUtils.copyProperties(user, entity, "name","role","phone");
			}
		}
		dao.save(entity);
	}

	@Override
	public Page<User> pageSearch(Pageable pageable, String searchText) {
		if(StringUtils.isNotBlank(searchText)){
			return dao.findAll(QUser.user.email.like("%"+searchText+"%").or(QUser.user.name.like("%"+searchText+"%")), pageable);
		}else {
			return dao.findAll(pageable);
		}
	}
	
	
}

package com.tivenwang.blog.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tivenwang.blog.dao.UserDao;
import com.tivenwang.blog.entity.User;

/** 
 * @author Pecan 
 * 类说明 
 */
@Service
public class UserLoginService {
	
	private static Logger logger=Logger.getLogger(UserLoginService.class);
	@Resource
	private UserDao userDao;
	
	public User getUser(String email,String pwd) throws Exception{
		return userDao.getUserByEmailPwd(email, pwd);
	}
	
}

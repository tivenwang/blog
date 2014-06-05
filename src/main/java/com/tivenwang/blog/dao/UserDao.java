package com.tivenwang.blog.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tivenwang.blog.entity.User;


/** 
 * @author Pecan 
 * 类说明 
 */
@Repository
public class UserDao extends BaseDAO {
	
	
	
	private static final String GET_USER_HQL="from User where email=:email and pwd=:pwd";
	public User getUserByEmailPwd(String email,String pwd) throws Exception{
		Map<Serializable, Serializable> map=new HashMap<Serializable, Serializable>(2);
		map.put("email", email);
		map.put("pwd", pwd);
		return super.findObjectByParameter(GET_USER_HQL, map);
	}
	
}

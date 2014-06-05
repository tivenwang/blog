package com.tivenwang.blog.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tivenwang.blog.config.GlobalConfig;
import com.tivenwang.blog.entity.User;
import com.tivenwang.blog.service.UserLoginService;

@Controller
@RequestMapping(value = "/user", method = RequestMethod.POST)
public class UserController {
	
	private static Logger logger=Logger.getLogger(UserController.class);
	
	@Resource
	private UserLoginService loginService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST,produces = {"application/x-www-form-urlencoded;charset=UTF-8"})
	@ResponseBody
	public String loginByuser(Model model,HttpServletRequest request, HttpServletResponse response) {
		 String userName= request.getParameter("userName");
		 String pwd= request.getParameter("pwd");
		 User user=null;
		 try {
			user=loginService.getUser(userName, pwd);
		} catch (Exception e) {
			logger.error(GlobalConfig.EMPTY_STRING, e);
		}
		 if (null==user) {
			 return "{status:0}";
		}else {
			return "{status:1}";
		}
	}
		
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logoutByuser(Model model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer userid=(Integer) request.getSession().getAttribute("mid");
		/*userService.updateUserLoginStatus(userid);
		request.getSession().removeAttribute("uid");
		request.getSession().removeAttribute("eid");
		request.getSession().removeAttribute("Name");*/
		request.getSession().invalidate();
		logger.info(userid);
		response.sendRedirect(((HttpServletRequest) request).getContextPath()+"/login.html");
	}

	@RequestMapping(value = "/loginName",
			method = RequestMethod.POST,produces = {"application/x-www-form-urlencoded;charset=UTF-8"})
	@ResponseBody
	public String lgetoginByuser(Model model,HttpServletRequest request, HttpServletResponse response) {
		Object name=request.getSession().getAttribute("Name");
		response.setCharacterEncoding("utf-8");  
		return name==null?"":name.toString();
		
	}	
	
}

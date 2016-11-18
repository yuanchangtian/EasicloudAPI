package com.ictwsn.user.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ictwsn.bean.UserBean;
import com.ictwsn.user.service.UserService;
import com.ictwsn.utils.Tools;
import com.ictwsn.utils.VerificationEMail;
import com.ictwsn.utils.redis.RedisOperation;

@Controller
@RequestMapping("/user")
public class UserAction {
	
	public static Logger logger = LoggerFactory.getLogger(UserAction.class);
	
	@Resource UserService userService;
	@Resource RedisOperation redisOperation;
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String createUser(HttpServletRequest request,HttpServletResponse response,UserBean userBean)
	{
		//生成UserKey
		String userKey = userBean.getUser_name()+System.currentTimeMillis()+userBean.getUser_password();
//				userBean.getUser_name()+System.currentTimeMillis()+userBean.getUser_password();
			try {
				userKey = Tools.EncrypSHA(userKey);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		userBean.setUser_key(userKey);
		//用户密码加密
			try {
				userBean.setUser_password(Tools.EncoderByMd5(userBean.getUser_password()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		//判断邮箱验证码
		String email_code = userService.getEmailCode(userBean.getUser_email());
		if(!email_code.equals(userBean.getUser_emailCode()))
		{
			request.setAttribute("message", "邮箱验证码不正确");
			return "redirect:/register.jsp";
		}
		//存储数据库
		userService.createUser(userBean);
		//获取user_id
		int user_id = userService.getUserId(userKey);
		//放入Redis中存储
		redisOperation.save(userKey, user_id);
		logger.info("user_key={},user_id={}",userKey,user_id);
		request.setAttribute("message", "注册成功！");
		return "redirect:/login.jsp";
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.GET)
	public String deleteUser(HttpServletRequest request,HttpServletResponse response,int user_id)
	{
		//权限检查，有更高一级的权限时，可删除该用户
		
		int i = userService.deleteUser(user_id);
		logger.info("成功删除用户记录：{}",i);
		return null;
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updateUser(HttpServletRequest request,HttpServletResponse response,UserBean userBean)
	{
		//权限检查，有更高一级的权限时，可删除该用户
		int i = userService.updateUser(userBean);
		logger.info("成功修改用户记录：{}",i);
		return null;
	}
	
	@RequestMapping(value="/select",method=RequestMethod.GET)
	public String selectUser(HttpServletRequest request,HttpServletResponse response,int user_id)
	{
		//检查登录状态
		
		//获取用户信息
		UserBean userBean = userService.getUser(user_id);
		logger.info("获取用户信息：{}",userBean.toString());
		//返回到客户端
		Tools.responseToJSON(response, userBean.toString());
		return null;
	}
	
	//修改用户密码
	@RequestMapping(value="/changepass",method=RequestMethod.POST)
	public String changePass(HttpServletRequest request,HttpServletResponse response,
			int user_id,String old_password,String new_password)
	{
		//检查登录状态
		
		String message = userService.changePass(user_id, old_password, new_password);
		logger.info(message);
		return null;
	}
	
	//邮箱验证
	@RequestMapping(value="/emailVerifaction",method=RequestMethod.GET)
	public String emailVerifaction(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "email" ,required = true) String email)
	{
		
		//自动生成6位数字验证码
		String email_code = getVerification();
		
		//返回邮箱状态，邮箱已存在或邮箱未存在已插入成功
		int i = userService.emailVerifaction(email, email_code);
		//可以注册，并返回验证码
		if(i==0)
		{
			//邮件发送
			VerificationEMail.mail(email_code, email);
			//消息返回
			responseToObjectJSON(response,"验证码已发送");
		}
		else if(i==1)
			responseToObjectJSON(response,"邮箱已经注册，请换其它邮箱");
		else
		{
			responseToObjectJSON(response,"邮箱验证码已再次发送，请查收");
		}
		return null;
	}
	//邮箱验证
		@RequestMapping(value="/test",method=RequestMethod.GET)
		public String userTest(HttpServletRequest request,HttpServletResponse response)
		{
			return "redirect:/register.jsp";  
		}
	
	
	//生成6位随机数
	public static String getVerification()
	{
		String random_code = "";
		for(int i=0;i<6;i++)
		{
			random_code += (int)(Math.random()*10);
		}
		return random_code;
	}
	
	//JSON封装返回数据
		public void responseToObjectJSON(HttpServletResponse response,String jsonContent)
		{
			response.setContentType("text/html;charset=utf-8");
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("msg", jsonContent);
				PrintWriter out = response.getWriter();
				out.write(jsonObject.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	
}

package com.ictwsn.user;

/**
 * 用户登录与退出类
 * 实现用户登录并记录当前session,保存当前记录，当用户退出后session会自动清除
 *
 * @version NES1.0 2014-08-17
 */
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ictwsn.bean.UserBean;
import com.ictwsn.user.service.UserService;

@Controller
public class loginAction {

	public static Logger logger = LoggerFactory.getLogger(loginAction.class);

	@Resource
	UserService userService;
	
	//已经登录后的刷新操作
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String returnIndex(
			HttpServletRequest request,
			HttpServletResponse response)throws IOException,ServletException
	{
		//进行验证
		HttpSession session = request.getSession(true);
		UserBean userBean = null;
		userBean = (UserBean) session.getAttribute("userlogin");
		if(userBean==null) return "login";
		else return "index";
	}

	// 用户登录
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	/**
	 * 用户登录
	 * @param request 客户端请求信息
	 * @param response 页面返回用户端的响应信息
	 * @param model 用于存储前后台交互数据的临时值
	 * @param user_name 用户名
	 * @param user_password 登录密码
	 * @param Verification 验证码
	 * @return 登录成功返回首页，登录失败显示error
	 */
	public String logincheck(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam(value = "user_name", required = true) String user_name,
			@RequestParam(value = "user_password", required = true) String user_password,
			@RequestParam(value = "remember_password", required = false) String remember_password)
			throws Exception
	// @RequestParam(value= "Verification" ,required = false) String
	// Verification) throws Exception

	{
		String checkResult = "用户名或密码错误";
		// if(request.getSession().getAttribute("rand")==null) return "login";
		// String code = request.getSession().getAttribute("rand").toString();

		// if(Verification==null) return "redirect:/index.do";

		// if(!Verification.equals(code))
		// {
		// checkResult = "验证码错误";
		// request.setAttribute("checkResult", checkResult);
		// return "login";
		// }

		user_name = user_name.trim();
		user_password = user_password.trim();

		Integer flag = 0;

		int i = userService.checkPassword(user_name, user_password);
		if (i == 0) {
			// 当登录验证成功时
			HttpSession session = request.getSession(true);
			UserBean userBean = null;
			userBean = (UserBean) session.getAttribute("userlogin");
			if (userBean == null) {
				// 取得用户信息并存入Bean中
				userBean = userService.getUserByName(user_name);
				// 用户存入会话当中
				session.setAttribute("userlogin", userBean);
				//是否需要存放到Cookie中，记住我时可存放Cookie
				if(remember_password!=null)
					CookieUtil.saveCookie(userBean, response);
			} else {
				checkResult = "已经登录了";
				// 已经登录，返回主页面
				// model.addAttribute("checkResult",checkResult.trim());
				// request.setAttribute("loginmessage", arg1);
				return "index";
			}
			checkResult = "登录成功";
			logger.info("checkResult={}", checkResult);
			request.setAttribute("checkResult", checkResult);
			request.setAttribute("user_name", user_name);
			return "index";
			// 登录成功后开始获取用户权限
			// 用户权限一般存放在list中

			/*
			 * logger.info(user_name+"登录成功");
			 * 
			 * int user_id = userRoleService.searchUidByName(user_name);
			 * 
			 * String user_idd = String.valueOf(user_id);
			 * LogDaoImpl.getInstance().setAid(user_idd);
			 * 
			 * RoleAuthority ra = new RoleAuthority(); ra =
			 * authorityService.searchRoleByUid(user_id); List<AuthorityInfo>
			 * list =
			 * authorityService.searchAuthorityByRole_Number(ra.getRole_number
			 * ());
			 * 
			 * int asl = 0;
			 * 
			 * //前台页面的url控制，每一个url对应一个权限. request.setAttribute("list", list);
			 * 
			 * request.setAttribute("user_name", user_name);
			 * request.setAttribute("checkResult", checkResult);
			 * request.setAttribute("user_id", user_id);
			 * request.setAttribute("role_id", ra.getRole_id());
			 * request.setAttribute("role_name", ra.getRole_name());
			 */
		} else {
			request.setAttribute("checkResult", checkResult);
			return "login";
		}

	}

	// 用户退出
	@RequestMapping("/exitAdmin.do")
	/**
	 * 用户退出
	 * @param request 客户端请求信息
	 * @param response 页面返回用户端的响应信息
	 * @param model 用于存储前后台交互数据的临时值
	 * @return 返回登录页面
	 */
	public String exitAdmin(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		HttpSession session = request.getSession(true);
		UserBean userBean = (UserBean) session.getAttribute("userlogin");
		if (userBean == null) {
			return "login";
		} else {
			//关闭会话
			request.getSession().invalidate();
			//清除Cookie
			CookieUtil.clearCookie(response);
			// LogDaoImpl.getInstance().free();
			logger.info("……………………………………用户已经退出");
			return "login";
		}
	}

}
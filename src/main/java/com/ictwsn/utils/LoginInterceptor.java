package com.ictwsn.utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ictwsn.bean.UserBean;
import com.ictwsn.user.CookieUtil;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Resource
	JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final String[] IGNORE_URI = { "/login.do","/user/emailVerifaction",
		"/user/create","/welcome","/jsontest"};
	public static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// 过滤掉特殊请求的URL
		String url = request.getRequestURL().toString();
		
		 logger.info(">>>: " + url);
		for (String s : IGNORE_URI) {
			if (url.contains(s)) {
				logger.info("{}请求放行",url);
				return true;
			}
		}
		// 再次过滤，判断是否为API请求
		String user_key = request.getHeader("user_key");
		// 若获取到user_key，则直接返回到API类执行相关操作

		if (user_key == null || user_key.equals("")) {
			// 若不为API请求，判断是否存在Session（即是否已登录)
			HttpSession session = request.getSession(true);
			UserBean userBean = (UserBean) session.getAttribute("userlogin");
			logger.info("用户登录状态：{}", userBean);
			if (userBean != null) {
				// 用户已经登录
				logger.info("用户已经登录：{}", userBean.getUser_name());
				// 允许访问
				return true;
			} else {
				// 用户未登录，启用Cookie检查是否满足自动登录
				int state = CookieUtil.readCookieAndLogon(request, response,
						jdbcTemplate);
				if (state == 0) {
					// 验证成功，允许操作
					logger.info("Cookie验证成功");
					return true;
				} else if (state == 1) {
					// 验证成功，允许操作
					logger.info("cookieValue为空");
					return false;
				} else if (state == 2) {
					// 验证成功，允许操作
					logger.info("非法操作进入本站");
					return false;
				} else if (state == 3) {
					// 验证成功，允许操作
					logger.info("Cookie失效，请重新登录");
					return false;
				} else if (state == 4) {
					// 验证成功，允许操作
					logger.info("Cookie验证错误");
					return false;
				} else {
					// 验证成功，允许操作
					logger.info("未查找到该Cookie用户");
					return false;
				}
			}
		} else {
			logger.info("用户API请求，user_key为：{}", user_key);
			return true;
		}

		/*
		 * String url = request.getRequestURL().toString(); //
		 * logger.info(">>>: " + url); for (String s : IGNORE_URI) { if
		 * (url.contains(s)) { flag = true; break; } }
		 * 
		 * //不包含初始URL会进行检测 if (!flag) { UserBean userBean =
		 * LoginController.getLoginUser(request); if (userBean != null) flag =
		 * true; } if(flag==true) logger.info("请求不拦截："+url); else
		 * logger.error("拦截请求："+url); return flag;
		 */
	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// super.postHandle(request, response, handler, modelAndView);
		String redirect_url = request
				.getRequestURL()
				.toString()
				.substring(0,
						request.getRequestURL().toString().lastIndexOf("/"));
		// logger.info("我也会执行:{}",jdbcTemplate==null);
		// response.sendRedirect(redirect_url+"/index2.jsp");
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// logger.info("在这里清理资源");
	}
}
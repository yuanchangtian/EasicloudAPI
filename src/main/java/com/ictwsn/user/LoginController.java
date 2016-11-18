package com.ictwsn.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ictwsn.bean.UserBean;

public class LoginController {
	public static UserBean getLoginUser(HttpServletRequest request)
	{
		HttpSession session=request.getSession(true);
		UserBean userBean=(UserBean)session.getAttribute("login");
		return userBean;
	}
}

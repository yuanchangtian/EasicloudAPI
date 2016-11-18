package com.ictwsn.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.ictwsn.bean.UserBean;
import com.ictwsn.utils.Base64;

public class CookieUtil {

	public static Logger logger = LoggerFactory.getLogger(CookieUtil.class);

	// 保存cookie时的cookieName
	private static String cookieDomainName = "easicloud";
	// 加密cookie时的网站自定码
	private final static String webKey = "easicloud";

	// 设置cookie有效期是两个星期，根据需要自定义
	private final static long cookieMaxAge = 60 * 60 * 24 * 7 * 2;

	// 保存Cookie到客户端-------------------------------------------------------------------------
	// 在CheckLogonServlet.java中被调用
	// 传递进来的user对象中封装了在登陆时填写的用户名与密码

	public static void saveCookie(UserBean userBean,
			HttpServletResponse response) {
		// cookie的有效期
		long validTime = System.currentTimeMillis() + (cookieMaxAge * 5000);

		// MD5加密用户详细信息
		String cookieValueWithMd5 = getMD5(userBean.getUser_name() + ":"
				+ userBean.getUser_password() + ":" + validTime + ":" + webKey);
		// 将要被保存的完整的Cookie值
		String cookieValue = userBean.getUser_name() + ":" + validTime + ":"
				+ cookieValueWithMd5;
		// 再一次对Cookie的值进行BASE64编码
		String cookieValueBase64 = Base64.encode(cookieValue);
		// 开始保存Cookie
		Cookie cookie = new Cookie(cookieDomainName, cookieValueBase64);
		// 存两年(这个值应该大于或等于validTime)
		cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
		// cookie有效路径是网站根目录
		cookie.setPath("/");
		// 向客户端写入
		response.addCookie(cookie);
		logger.info("已存储用户Cookie：{}",userBean.getUser_name());
	}

	// 读取Cookie,自动完成登陆操作----------------------------------------------------------------
	// 在Filter程序中调用该方法,见AutoLogonFilter.java
	public static int readCookieAndLogon(HttpServletRequest request,
			HttpServletResponse response, JdbcTemplate jdbcTemplate)
			throws IOException, ServletException, UnsupportedEncodingException {
		// 根据cookieName取cookieValue
		Cookie cookies[] = request.getCookies();
		String cookieValue = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookieDomainName.equals(cookies[i].getName())) {
					cookieValue = cookies[i].getValue();
					break;
				}
			}
		}
		// 如果cookieValue为空,返回,
		if (cookieValue == null) {
			return 1;
		}
		// 如果cookieValue不为空,才执行下面的代码
		// 先得到的CookieValue进行Base64解码
		// String cookieValueAfterDecode = new
		// String(Base64.decode(cookieValue),"utf-8");
		String cookieValueAfterDecode = Base64.decode(cookieValue);
		// 对解码后的值进行分拆,得到一个数组,如果数组长度不为3,就是非法登陆
		String cookieValues[] = cookieValueAfterDecode.split(":");
		if (cookieValues.length != 3) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("你正在用非正常方式进入本站...");
			out.close();
			return 2;
		}
		// 判断是否在有效期内,过期就删除Cookie
		long validTimeInCookie = new Long(cookieValues[1]);
		if (validTimeInCookie < System.currentTimeMillis()) {
			// 删除Cookie
			clearCookie(response);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("你的Cookie已经失效,请重新登陆");
			out.close();
			return 3;
		}
		// 取出cookie中的用户名,并到数据库中检查这个用户名,
		String user_name = cookieValues[0];
		// 根据用户名到数据库中检查用户是否存在，并取得该用户的所有信息

		String sql = "select * from tbl_user where user_name = ?";
		ResultSetExtractor<UserBean> rse = new ResultSetExtractor<UserBean>() {
			@Override
			public UserBean extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				UserBean userBean = new UserBean();
				while (rs.next()) {
					userBean.setNick_name(rs.getString("nick_name"));
					userBean.setUser_email(rs.getString("user_email"));
					userBean.setUser_id(rs.getInt("user_id"));
					userBean.setUser_name(rs.getString("user_name"));
					userBean.setUser_phone(rs.getString("user_phone"));
					userBean.setUser_key(rs.getString("user_key"));
					userBean.setUser_role(rs.getInt("user_role"));
					userBean.setUser_password(rs.getString("user_password"));
				}
				return userBean;
			}
		};
		UserBean userBean = jdbcTemplate.query(sql, rse,
				new Object[] { user_name });

		// 如果user返回不为空,就取出密码,使用用户名+密码+有效时间+ webSiteKey进行MD5加密
		if (userBean.getUser_name() != null
				&& userBean.getUser_password() != null) {
			String md5ValueInCookie = cookieValues[2];
			String md5ValueFromUser = getMD5(userBean.getUser_name() + ":"
					+ userBean.getUser_password() + ":" + validTimeInCookie
					+ ":" + webKey);
			// 将结果与Cookie中的MD5码相比较,如果相同,写入Session,自动登陆成功,并继续用户请求
			if (md5ValueFromUser.equals(md5ValueInCookie)) {
				HttpSession session = request.getSession(true);
				session.setAttribute("userlogin", userBean);
				return 0;
				// chain.doFilter(request, response);
		} else {
			// 返回为空执行
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("cookie验证错误！");
			out.close();
			return 4;
			}
		}
		else
		{
			//未查找到该用户
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("未查找到该用户！"+user_name);
			out.close();
			return 5;
		}
	}

	// 用户注销时,清除Cookie,在需要时可随时调用-----------------------------------------------------
	public static void clearCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieDomainName, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		logger.info("Cookie清除完毕");
	}

	// 获取Cookie组合字符串的MD5码的字符串----------------------------------------------------------------
	public static String getMD5(String value) {
		String result = null;
		try {
			byte[] valueByte = value.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(valueByte);
			result = toHex(md.digest());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	// 将传递进来的字节数组转换成十六进制的字符串形式并返回
	private static String toHex(byte[] buffer) {
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
		}
		return sb.toString();
	}

}

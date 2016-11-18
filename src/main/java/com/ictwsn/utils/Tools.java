package com.ictwsn.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ictwsn.bean.UserBean;

import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class Tools {
	//MD5加密
	public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		 //确定计算方法
		  MessageDigest md5=MessageDigest.getInstance("MD5");
		  BASE64Encoder base64en = new BASE64Encoder();
		 //加密后的字符串
		  String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
		  return newstr;
	}
	//SHA加密
	 public static String EncrypSHA(String info) throws NoSuchAlgorithmException{
		 //简短加密
	       /* MessageDigest sha = MessageDigest.getInstance("SHA");  
	        byte[] srcBytes = info.getBytes();  
	        //使用srcBytes更新摘要  
	        sha.update(srcBytes);  
	        //完成哈希计算，得到result  
	        byte[] resultBytes = sha.digest();
	        return resultBytes.toString();  */
		 //40位加密
		 MessageDigest sha = null;
		 StringBuffer hexValue = new StringBuffer();
	        try {
	            sha = MessageDigest.getInstance("SHA");
	        } catch (Exception e) {
	            System.out.println(e.toString());
	            e.printStackTrace();
	            return "";
	        }

	        byte[] byteArray;
			try {
				byteArray = info.getBytes("UTF-8");
				byte[] md5Bytes = sha.digest(byteArray);
		        for (int i = 0; i < md5Bytes.length; i++) {
		            int val = ((int) md5Bytes[i]) & 0xff;
		            if (val < 16) { 
		                hexValue.append("0");
		            }
		            hexValue.append(Integer.toHexString(val));
		        }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	        return hexValue.toString();
		 
	   }  
	//用于消除Mongodb中8个小时的时间误差
	public static Date changeToLocalChina(Date datetime)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datetime);
		calendar.add(Calendar.HOUR, 8);
		datetime = calendar.getTime();
		return datetime;
	}
	//Date转为String
	public static String dateToString(Date datetime)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(datetime);
	}
	
	//String转为Date
	public static Date stringToDate(String datetime)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	//把字符串转化为东8区世家
	public static Date changeToDate8(String timestamp)
	{
		if(timestamp==null||timestamp.equals("")||timestamp.equals("null")) return null;
		timestamp = timestamp.replace("T", " ");//将里面的T替换成空格
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(timestamp);
			date = changeToLocalChina(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	//把字符串转化为东8区世家
		public static Date changeToDate8(Date timestamp)
		{
			if(timestamp==null||timestamp.equals("")||timestamp.equals("null")) return null;
			Date date = null;
			date = changeToLocalChina(timestamp);
			return date;
		}
	//将Long转化为Date
	
		
	//得到当前的东8区时间
	public static Date getNowDate8()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//JSON封装返回数据
		public static void responseToJSON(HttpServletResponse response,String jsonContent)
		{
			response.setContentType("text/html;charset=utf-8");
			try {
				PrintWriter out = response.getWriter();
				out.write(jsonContent);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//获得用户的Session
		public static UserBean getSessionUserBean(HttpServletRequest request)
		{
			HttpSession session = request.getSession(true);
			return (UserBean) session.getAttribute("userlogin");
		}
		//创建分页对象
		public static Page getPage(int perCount,int allCount,String currentPageStr)
		{
			//页码获取
			int currentPage = 0;
			if(currentPageStr == null || "".equals(currentPageStr)){
					currentPage = 1;
			}else {
					currentPage = Integer.parseInt(currentPageStr);
			}
			return PageUtil.createPage(perCount,allCount, currentPage);
		}
	
	public static void main(String[] args) {
		/*try {
			System.out.println(EncrypSHA("easicloudjljajdfio324jll38hl3"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(sdf.format(new Date()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}*/
		System.out.println(getNowDate8());
	}
	
}

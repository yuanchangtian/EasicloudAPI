package com.ictwsn.user.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ictwsn.bean.UserBean;
import com.ictwsn.user.action.UserAction;
import com.ictwsn.user.dao.UserDao;
import com.ictwsn.utils.BaseDao;
import com.ictwsn.utils.Tools;
import com.ictwsn.utils.VerificationEMail;

@Service
public class UserServiceImpl extends BaseDao implements UserService{
	
	public static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	public UserDao init()
	{
		UserDao userDao = this.sqlSessionTemplate.getMapper(UserDao.class);
		return userDao;
	}

	public boolean createUser(UserBean userBean) {
		int i = init().createUser(userBean);
		if(i>0)
		{
			//当创建成功时更改邮箱验证
			init().updateEmailVerify(userBean.getUser_email());
		}
		//否则直接返回
		else
		{
			logger.info("创建用户{}失败",userBean.getUser_name());
		}
		return true;
	}

	public int getUserId(String user_key) {
		return init().getUserId(user_key);
	}

	public int deleteUser(int user_id) {
		int i = init().deleteUser(user_id);
		return i;
	}

	public int updateUser(UserBean userBean) {
		int i = init().updateUser(userBean);
		return i;
	}

	public UserBean getUser(int user_id) {
		return init().getUser(user_id);
	}

	public String changePass(int user_id, String old_password, String new_password) {
		
		//获取数据库密码
		String db_password = init().getPassword(user_id);
		//密码判断
			
				try {
					if(db_password.equals(Tools.EncoderByMd5(old_password)))
					{
						//密码修改
						int i = init().changePassword(Tools.EncoderByMd5(new_password), user_id);
						if(i==1) return "密码修改成功";
						else return "密码修改失败";
					}
					else return "原密码不正确";
				} catch (NoSuchAlgorithmException
						| UnsupportedEncodingException e) {
					e.printStackTrace();
					return e.getMessage();
				}
	}

	@Override
	public int checkPassword(String user_name, String user_password) {
		//获取数据库密码
		String db_password = init().getPasswordByName(user_name);
		if(db_password==null||db_password.equals("")) return 1;
		try {
				if(db_password.equals(Tools.EncoderByMd5(user_password)))
				{
					System.out.println("我执行了");
					//返回0表示用户名密码正确
					return 0;
				}
				//否则不正确
				else return 1;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return 1;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 1;
		}
	}

	@Override
	public int emailVerifaction(String email, String email_code) {
		
		//获取当前时间，并向后添加1分钟
		Calendar cal = Calendar.getInstance();
		cal.setTime(Tools.getNowDate8());
		cal.add(Calendar.MINUTE,1);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = cal.getTime();
		int i = init().getEmailCount(email,1);
		//查找到已经注册的邮箱，则不允再注册，返回1
		if(i>0) return 1;
		else
		{
			//检查邮件是否存在，若存在则不允插入
			int k = init().getEmailCount(email,0);
			if(k>0)
			{
				//并更改最新时间
				//获取当前时间，并向后添加1分钟
				cal = Calendar.getInstance();
				cal.setTime(Tools.getNowDate8());
				cal.add(Calendar.MINUTE,1);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = cal.getTime();
				email_code = UserAction.getVerification();
				init().updateEmailVerifyDateCode(email,date,email_code);
				VerificationEMail.mail(email_code, email);
				return 2;
			}
			//插入邮箱和验证码，并发送消息，允许注册
			int j = init().insertEmailVerify(email, email_code,date);
			logger.info("已成功插入邮箱和验证码条数：{}",j);
			return 0;
		}
	}

	@Override
	public int judgeEmailDatetime(String email, Date deadline) {
		return init().judgeEmailDatetime(email, deadline);
	}

	@Override
	public String getEmailCode(String email) {
		return init().getEmailCode(email);
	}

	@Override
	public UserBean getUserByName(String user_name) {
		return init().getUserByName(user_name);
	}


}

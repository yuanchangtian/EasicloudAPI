package com.ictwsn.user.service;

import java.util.Date;

import com.ictwsn.bean.UserBean;

public interface UserService {
	public boolean createUser(UserBean userBean);
	public int getUserId(String user_key);
	public int deleteUser(int user_id);
	public int updateUser(UserBean userBean);
	public UserBean getUser(int user_id);
	public String changePass(int user_id,String old_password,String new_password);
	public int checkPassword(String user_name,String user_password);
	public int emailVerifaction(String email,String email_code);
	public int judgeEmailDatetime(String email,Date deadline);
	public String getEmailCode(String email);
	public UserBean getUserByName(String user_name);
}

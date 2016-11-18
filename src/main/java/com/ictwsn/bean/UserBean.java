package com.ictwsn.bean;

public class UserBean {
	private int user_id;
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "UserBean [user_id=" + user_id + ", user_name=" + user_name
				 + ", user_key=" + user_key
				+ ", user_role=" + user_role + ", user_phone=" + user_phone
				+ ", user_email=" + user_email + ", nick_name=" + nick_name
				+ "]";
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_key() {
		return user_key;
	}
	public void setUser_key(String user_key) {
		this.user_key = user_key;
	}
	public int getUser_role() {
		return user_role;
	}
	public void setUser_role(int user_role) {
		this.user_role = user_role;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	private String user_name;
	private String user_password;
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	private String user_key;
	private int user_role;
	private String user_phone;
	private String user_email;
	private String nick_name;
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	private String user_emailCode;
	public String getUser_emailCode() {
		return user_emailCode;
	}
	public void setUser_emailCode(String user_emailCode) {
		this.user_emailCode = user_emailCode;
	}
	
}

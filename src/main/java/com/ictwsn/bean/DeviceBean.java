package com.ictwsn.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class DeviceBean {
	private int device_id;
	public int getDevice_id() {
		return device_id;
	}
	@Override
	public String toString() {
		return "DeviceBean [device_id=" + device_id + ", user_id=" + user_id
				+ ", device_active=" + device_active + ", device_name="
				+ device_name + ", device_created=" + device_created
				+ ", device_description=" + device_description
				+ ", device_publicity=" + device_publicity
				+ ", device_location=" + device_location + "]";
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getDevice_active() {
		return device_active;
	}
	public void setDevice_active(int device_active) {
		this.device_active = device_active;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public Date getDevice_created() {
		return device_created;
	}
	public void setDevice_created(Date device_created) {
		this.device_created = device_created;
	}
	public String getDevice_description() {
		return device_description;
	}
	public void setDevice_description(String device_description) {
		this.device_description = device_description;
	}
	public int getDevice_publicity() {
		return device_publicity;
	}
	public void setDevice_publicity(int device_publicity) {
		this.device_publicity = device_publicity;
	}
	public String getDevice_location() {
		return device_location;
	}
	public void setDevice_location(String device_location) {
		this.device_location = device_location;
	}
	private int user_id;
	private int device_active;
	private String device_name;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	private Date device_created;
	private String device_description;
	private int device_publicity;
	private String device_location;
	private String device_created_format;
//	@NumberFormat(pattern="#,###.##") 数字转换
	public String getDevice_created_format() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		return sdf.format(device_created);
	}
	public void setDevice_created_format(String device_created_format) {
		this.device_created_format = device_created_format;
	}
}

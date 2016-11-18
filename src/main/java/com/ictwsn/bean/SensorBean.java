package com.ictwsn.bean;

public class SensorBean {
	private int sensor_id;
	public int getSensor_id() {
		return sensor_id;
	}
	@Override
	public String toString() {
		return "SensorBean [sensor_id=" + sensor_id + ", device_id="
				+ device_id + ", sensor_name=" + sensor_name
				+ ", sensor_description=" + sensor_description
				+ ", sensor_imgurl=" + sensor_imgurl + ", sensor_data="
				+ sensor_data + ", sensor_values=" + sensor_values + "]";
	}
	public void setSensor_id(int sensor_id) {
		this.sensor_id = sensor_id;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public String getSensor_name() {
		return sensor_name;
	}
	public void setSensor_name(String sensor_name) {
		this.sensor_name = sensor_name;
	}
	public String getSensor_description() {
		return sensor_description;
	}
	public void setSensor_description(String sensor_description) {
		this.sensor_description = sensor_description;
	}
	public String getSensor_imgurl() {
		return sensor_imgurl;
	}
	public void setSensor_imgurl(String sensor_imgurl) {
		this.sensor_imgurl = sensor_imgurl;
	}
	public String getSensor_data() {
		return sensor_data;
	}
	public void setSensor_data(String sensor_data) {
		this.sensor_data = sensor_data;
	}
	private int device_id;
	private String sensor_name;
	private String sensor_description;
	private String sensor_imgurl;
	private String sensor_data;
	private String sensor_values;
	public String getSensor_values() {
		return sensor_values;
	}
	public void setSensor_values(String sensor_values) {
		this.sensor_values = sensor_values;
	}
	
}

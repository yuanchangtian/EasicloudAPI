package com.ictwsn.sensor.service;

import java.util.List;

import com.ictwsn.bean.SensorBean;
import com.ictwsn.utils.Page;

public interface SensorManageService {
	public int geSensorsCount(int user_id);
	public List<SensorBean> showSensors(int user_id,Page page);
	public int updateSensor(SensorBean sensorBean);
	public int insertSensor(SensorBean sensorBean);
	public int deleteSensor(int sensor_id,int device_id);
}

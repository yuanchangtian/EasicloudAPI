package com.ictwsn.api.sensor;

import com.ictwsn.bean.SensorBean;

public interface SensorService {
	public int createSensor(SensorBean sensorBean);
	public String getSensorsInformation(int user_id,int device_id,String object_id);
	public String listSensors(int user_id,int device_id);
	public String sensorShow(int user_id,int device_id,int sensor_id);
	public int sensorUpdate(int user_id,SensorBean sensorBean); 
	public int sensorDelete(int user_id,int device_id,int sensor_id);
	
}

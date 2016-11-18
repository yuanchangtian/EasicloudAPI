package com.ictwsn.api.datapoint;

import org.apache.ibatis.annotations.Select;

public interface DatapointDao {
	
	//判断用户是否对应有该设备下的传感器
	@Select("select count(*) from tbl_device,tbl_sensor where tbl_device.user_id=#{0} and tbl_device.device_id=#{1} and tbl_device.device_id = tbl_sensor.device_id and tbl_sensor.sensor_id = #{2}")
	public int judgeUserIdSensorId(int user_id,int device_id,int sensor_id);
	
	//判断用户是否对应有该设备下的传感器
	@Select("select count(*) from tbl_device,tbl_sensor where tbl_device.user_id=#{0} and tbl_device.device_id = tbl_sensor.device_id and tbl_sensor.sensor_id = #{1}")
	public int judgeUserIdSensorIdSimple(int user_id,int sensor_id);
	
}

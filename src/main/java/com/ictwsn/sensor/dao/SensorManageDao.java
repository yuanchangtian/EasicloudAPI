package com.ictwsn.sensor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ictwsn.bean.SensorBean;
import com.ictwsn.utils.Page;

public interface SensorManageDao {
	
	//查询该用户下的所有传感器数量
	@Select("select count(*) from tbl_device,tbl_sensor where tbl_device.user_id=#{0} "
			+ "and tbl_device.device_id = tbl_sensor.device_id")
	public int geSensorsCount(int user_id); 
	
	//查询该用户下的所有传感器
	@Select("select * from tbl_device,tbl_sensor where tbl_device.user_id=#{user_id} "
			+ "and tbl_device.device_id = tbl_sensor.device_id ORDER BY sensor_id DESC limit #{page.beginIndex},#{page.everyPage}")
	public List<SensorBean> showSensors(@Param("user_id")int user_id,@Param("page")Page page);
	
	//更新传感器数据
	@Update("update tbl_sensor set sensor_name=#{sensor_name},sensor_description=#{sensor_description},sensor_imgurl=#{sensor_imgurl},"
			+ "sensor_data=#{sensor_data},sensor_values=#{sensor_values} where sensor_id=#{sensor_id} and device_id=#{device_id}")
	public int updateSensor(SensorBean sensorBean);
	
	//删除传感器数据
	@Delete("delete from tbl_sensor where sensor_id=#{0} and device_id=#{1}")
	public int deleteSensor(int sensor_id,int device_id);
	
	//插入传感器数据
	@Insert("insert into tbl_sensor (device_id,sensor_name,sensor_description,sensor_imgurl,sensor_data,"
			+ "sensor_values) values(#{device_id},#{sensor_name},#{sensor_description},#{sensor_imgurl},#{sensor_data},#{sensor_values})")
	public int insertSensor(SensorBean sensorBean);
	
}

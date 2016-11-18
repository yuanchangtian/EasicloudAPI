package com.ictwsn.api.sensor;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ictwsn.bean.SensorBean;

public interface SensorDao {
	//创建传感器
	@Insert("insert into tbl_sensor (device_id,sensor_name,sensor_description,sensor_imgurl,"
			+"sensor_data,sensor_values) values(#{device_id},#{sensor_name},#{sensor_description},"
			+"#{sensor_imgurl},#{sensor_data},#{sensor_values})")
	public void createSensor(SensorBean sensorBean);
	
	//返回最新创建的传感器id
	@Select("select sensor_id from tbl_sensor where device_id = #{device_id} order by sensor_id desc limit 0,1")
	public int getLastSensorId(int device_id);
	
	//返回某设备下所有传感器的信息
	@Select("select * from tbl_sensor where device_id = #{device_id}")
	public List<SensorBean> getSensorsInformation(int device_id);
	
	//罗列传感器
	@Select("select * from tbl_sensor,tbl_device where tbl_device.user_id=#{0} and tbl_sensor.device_id=tbl_device.device_id and tbl_sensor.device_id=#{1}")
	public List<SensorBean> listSensors(int user_id, int device_id);
	
	//获得某个传感器的具体信息
	@Select("SELECT * FROM tbl_sensor,tbl_device where tbl_device.user_id=#{0} and tbl_sensor.device_id=tbl_device.device_id and tbl_sensor.device_id=#{1} and tbl_sensor.sensor_id=#{2}")
	public SensorBean sensorShow(int user_id, int device_id,int sensor_id);
	
	//修改某个传感器的信息，注解参数和对象参数同时传入
	@Update("update tbl_sensor,tbl_device set tbl_sensor.sensor_name=#{sensorBean.sensor_name},"
			+ "tbl_sensor.sensor_description=#{sensorBean.sensor_description},tbl_sensor.sensor_imgurl=#{sensorBean.sensor_imgurl},tbl_sensor.sensor_data=#{sensorBean.sensor_data},tbl_sensor.sensor_values=#{sensorBean.sensor_values}"
			+ " where  tbl_device.user_id=#{user_id} and tbl_sensor.device_id=tbl_device.device_id and tbl_sensor.device_id=#{sensorBean.device_id} and tbl_sensor.sensor_id=#{sensorBean.sensor_id}")
	public int sensorUpdate(@Param("user_id")int user_id,@Param("sensorBean")SensorBean sensorBean);
	
	//删除某个传感器
	@Delete("DELETE from tbl_sensor where sensor_id ="
			+ "(select temp.sensor_id from (select * from tbl_sensor) "
			+ "as temp,tbl_device where tbl_device.device_id=temp.device_id and "
			+ "tbl_device.user_id=#{0} and temp.sensor_id=#{2} and tbl_device.device_id=#{1})")
	public int sensorDelete(int user_id,int device_id,int sensor_id);
	
	//查询传感器中的公用数据信息
	@Select("SELECT sensor_data FROM tbl_sensor,tbl_device where tbl_device.user_id=#{0} and tbl_sensor.device_id=tbl_device.device_id and tbl_sensor.sensor_id=#{1}")
	public String sensorDataShow(int user_id,int sensor_id);
	
}

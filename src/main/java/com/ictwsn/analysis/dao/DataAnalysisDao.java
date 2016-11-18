package com.ictwsn.analysis.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ictwsn.bean.SensorBean;

public interface DataAnalysisDao {
	
	//查询该用户下的第一个传感器id
		@Select("select * from tbl_device,tbl_sensor where tbl_device.user_id=#{user_id} "
				+ "and tbl_device.device_id = tbl_sensor.device_id limit 1")
		public SensorBean getFirstSensorId(@Param("user_id")int user_id);
		
}

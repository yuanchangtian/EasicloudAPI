package com.ictwsn.sensor.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ictwsn.bean.SensorBean;
import com.ictwsn.sensor.action.SensorManageAction;
import com.ictwsn.sensor.dao.SensorManageDao;
import com.ictwsn.utils.BaseDao;
import com.ictwsn.utils.Page;

@Service
public class SensorManageServiceDaoImpl extends BaseDao implements SensorManageService {
	
	public static Logger logger = LoggerFactory.getLogger(SensorManageAction.class);
	
	public SensorManageDao init()
	{
		SensorManageDao sensorDao = this.sqlSessionTemplate.getMapper(SensorManageDao.class);
		return sensorDao;
	}

	public int geSensorsCount(int user_id) {
		return this.init().geSensorsCount(user_id);
	}

	public List<SensorBean> showSensors(int user_id, Page page) {
		return this.init().showSensors(user_id, page);
	}

	public int updateSensor(SensorBean sensorBean) {
		return this.init().updateSensor(sensorBean);
	}

	public int insertSensor(SensorBean sensorBean) {
		return this.init().insertSensor(sensorBean);
	}

	public int deleteSensor(int sensor_id, int device_id) {
		return this.init().deleteSensor(sensor_id, device_id);
	}

	
	
}

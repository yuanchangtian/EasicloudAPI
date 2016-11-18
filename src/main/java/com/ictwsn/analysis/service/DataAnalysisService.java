package com.ictwsn.analysis.service;

import net.sf.json.JSONArray;

import com.ictwsn.bean.SensorBean;

public interface DataAnalysisService {
	public SensorBean getFirstSensorId(int user_id);
	public JSONArray get24HoursDatapoints(int user_id, int sensor_id, String value_type);
}

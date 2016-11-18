package com.ictwsn.analysis.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ictwsn.analysis.dao.DataAnalysisDao;
import com.ictwsn.bean.SensorBean;
import com.ictwsn.utils.BaseDao;
import com.ictwsn.utils.Tools;
import com.mongodb.DBObject;

@Service
public class DataAnalysisServiceImpl extends BaseDao implements
		DataAnalysisService {

	public static Logger logger = LoggerFactory
			.getLogger(DataAnalysisServiceImpl.class);

	public DataAnalysisDao init() {
		DataAnalysisDao dataAnalysisDao = this.sqlSessionTemplate
				.getMapper(DataAnalysisDao.class);
		return dataAnalysisDao;
	}

	@Override
	public SensorBean getFirstSensorId(int user_id) {
		return this.init().getFirstSensorId(user_id);
	}

	public JSONArray get24HoursDatapoints(int user_id, int sensor_id,
			String value_type) {

		Date end = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		calendar.add(Calendar.MINUTE, -10);
		Date start = calendar.getTime();
		start = Tools.changeToDate8(start);
		end = Tools.changeToDate8(end);
		System.out.println(start);
		System.out.println(end);
		
		Criteria criatira = new Criteria();
		if (sensor_id > 0 && start != null && end != null) {
			criatira.andOperator(
					Criteria.where("sensor_id").is(sensor_id),
					Criteria.where("timestamp").gte(start)
							.andOperator(Criteria.where("timestamp").lte(end)));
		}

		else if (sensor_id > 0 && start == null && end == null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id));
		else if (sensor_id > 0 && start != null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id),
					Criteria.where("timestamp").gte(start));
		else if (sensor_id > 0 && end != null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id),
					Criteria.where("timestamp").lte(end));
		else
			criatira = null;

		List<DBObject> list = this.mongoTemplate.find(new Query(criatira),
				DBObject.class, String.valueOf(user_id));

		System.out.println(list.size());
		JSONArray jsonArray = JSONArray.fromObject(list);
		JSONArray valueArray = new JSONArray();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray arrayDate = new JSONArray();
			JSONObject jo = jsonArray.getJSONObject(i);
			arrayDate.add(Long.parseLong(jo.getJSONObject("_id").getString("time")));
			
//			System.out.println(Long.parseLong(jo.getJSONObject("_id").getString("time")));
			double value_temp = Double.parseDouble(jo.getJSONArray("values").getJSONObject(0)
					.getString("temperature"));
			arrayDate.add(value_temp);
			valueArray.add(arrayDate);
		}
		return valueArray;
	}

}

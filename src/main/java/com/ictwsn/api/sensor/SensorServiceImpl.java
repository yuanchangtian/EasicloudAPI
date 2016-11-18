package com.ictwsn.api.sensor;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ictwsn.api.datapoint.DatapointService;
import com.ictwsn.bean.SensorBean;
import com.ictwsn.utils.BaseDao;

@Service
public class SensorServiceImpl extends BaseDao implements SensorService{

	@Resource DatapointService datapointService;
	
	public int createSensor(SensorBean sensorBean) {
		SensorDao sensorDao = this.sqlSessionTemplate.getMapper(SensorDao.class);
		sensorDao.createSensor(sensorBean);
		return sensorDao.getLastSensorId(sensorBean.getDevice_id());
	}
	//获得传感器所有的信息，并以JSON字符串的形式返回
	public String getSensorsInformation(int user_id,int device_id,String object_id) {
		SensorDao sensorDao = this.sqlSessionTemplate.getMapper(SensorDao.class);
		List<SensorBean> list = sensorDao.getSensorsInformation(device_id);
		JSONObject sensorsObject = new JSONObject();
		sensorsObject.put("user_id", user_id);
		sensorsObject.put("device_id", device_id);
		JSONArray sensorsArray = new JSONArray();
		//依次获得Mongodb中所对应的传感器id数据
		for(SensorBean sensorBean : list)
		{
			//获得数据点集
			String datapoints = datapointService.getDatapoints(user_id, sensorBean.getSensor_id(), null, null,object_id);
			//封装所对应的传感器信息
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sensor_id", sensorBean.getSensor_id());
			jsonObject.put("sensor_name", sensorBean.getSensor_name());
			jsonObject.put("sensor_description", sensorBean.getSensor_description());
			jsonObject.put("sensor_imgurl", sensorBean.getSensor_imgurl());
			//处理公用信息
			if(sensorBean.getSensor_data().contains(";"))
			{
				JSONArray data_array = new JSONArray();
				String[] s_data = sensorBean.getSensor_data().split(";");
				for(String s : s_data)
				{
					JSONObject s_object = new JSONObject();
					if(s.contains("|"))
					{
						String[] unit_symbol = s.split("\\|");
						s_object.put("unit", unit_symbol[0]);
						s_object.put("symbol", unit_symbol[1]);
					}
					else s_object.put("unit", s) ;
					data_array.add(s_object);
				}
				jsonObject.put("sensor_data", data_array);
			}
			else jsonObject.put("sensor_data", "");
			//处理数据信息
			if(sensorBean.getSensor_values().contains(";"))
			{
				JSONArray data_array = new JSONArray();
				String[] s_valus = sensorBean.getSensor_values().split(";");
				for(String s : s_valus)
				{
					JSONObject s_object = new JSONObject();
					if(s.contains("|"))
					{
						String[] unit_symbol = s.split("\\|");
						s_object.put("unit", unit_symbol[0]);
						s_object.put("symbol", unit_symbol[1]);
					}
					else s_object.put("unit", s) ;
					data_array.add(s_object);
				}
				jsonObject.put("sensor_values", data_array);
			}
			else jsonObject.put("sensor_values", "");
			//封装数据点集
			JSONArray jsonArray = JSONArray.fromObject(datapoints);
			jsonObject.put("datapoint_count", jsonArray.size());
			jsonObject.put("datapoints", jsonArray);
			//存入整个设备下的JSONArray中
			sensorsArray.add(jsonObject);
		}
		sensorsObject.put("sensors", sensorsArray);
		return sensorsObject.toString();
	}
	public String listSensors(int user_id, int device_id) {
		SensorDao sensorDao = this.sqlSessionTemplate.getMapper(SensorDao.class);
		List<SensorBean> list = sensorDao.listSensors(user_id, device_id);
		JSONObject sensorsObject = new JSONObject();
		sensorsObject.put("user_id", user_id);
		sensorsObject.put("device_id", device_id);
		JSONArray sensorsArray = new JSONArray();
		//依次获得Mongodb中所对应的传感器id数据
		for(SensorBean sensorBean : list)
		{
			//获得数据点集
			//封装所对应的传感器信息
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sensor_id", sensorBean.getSensor_id());
			jsonObject.put("sensor_name", sensorBean.getSensor_name());
			jsonObject.put("sensor_description", sensorBean.getSensor_description());
			jsonObject.put("sensor_imgurl", sensorBean.getSensor_imgurl());
			//处理公用信息
			if(sensorBean.getSensor_data().contains(";"))
			{
				JSONArray data_array = new JSONArray();
				String[] s_data = sensorBean.getSensor_data().split(";");
				for(String s : s_data)
				{
					JSONObject s_object = new JSONObject();
					if(s.contains("|"))
					{
						String[] unit_symbol = s.split("\\|");
						s_object.put("unit", unit_symbol[0]);
						s_object.put("symbol", unit_symbol[1]);
					}
					else s_object.put("unit", s) ;
					data_array.add(s_object);
				}
				jsonObject.put("sensor_data", data_array);
			}
			else jsonObject.put("sensor_data", "");
			//处理数据信息
			if(sensorBean.getSensor_values().contains(";"))
			{
				JSONArray data_array = new JSONArray();
				String[] s_valus = sensorBean.getSensor_values().split(";");
				for(String s : s_valus)
				{
					JSONObject s_object = new JSONObject();
					if(s.contains("|"))
					{
						String[] unit_symbol = s.split("\\|");
						s_object.put("unit", unit_symbol[0]);
						s_object.put("symbol", unit_symbol[1]);
					}
					else s_object.put("unit", s) ;
					data_array.add(s_object);
				}
				jsonObject.put("sensor_values", data_array);
			}
			else jsonObject.put("sensor_values", "");
			//存入整个设备下的JSONArray中
			sensorsArray.add(jsonObject);
		}
		sensorsObject.put("sensor_count", sensorsArray.size());
		sensorsObject.put("sensors", sensorsArray);
		return sensorsObject.toString();
	}
	public String sensorShow(int user_id, int device_id, int sensor_id) {
		SensorDao sensorDao = this.sqlSessionTemplate.getMapper(SensorDao.class);
		SensorBean sensorBean = sensorDao.sensorShow(user_id, device_id, sensor_id);
		JSONObject sensorsObject = new JSONObject();
		sensorsObject.put("user_id", user_id);
		sensorsObject.put("device_id", device_id);
		//依次获得Mongodb中所对应的传感器id数据
			//获得数据点集
			//封装所对应的传感器信息
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sensor_id", sensorBean.getSensor_id());
			jsonObject.put("sensor_name", sensorBean.getSensor_name());
			jsonObject.put("sensor_description", sensorBean.getSensor_description());
			jsonObject.put("sensor_imgurl", sensorBean.getSensor_imgurl());
			//处理公用信息
			if(sensorBean.getSensor_data().contains(";"))
			{
				JSONArray data_array = new JSONArray();
				String[] s_data = sensorBean.getSensor_data().split(";");
				for(String s : s_data)
				{
					JSONObject s_object = new JSONObject();
					if(s.contains("|"))
					{
						String[] unit_symbol = s.split("\\|");
						s_object.put("unit", unit_symbol[0]);
						s_object.put("symbol", unit_symbol[1]);
					}
					else s_object.put("unit", s) ;
					data_array.add(s_object);
				}
				jsonObject.put("sensor_data", data_array);
			}
			else jsonObject.put("sensor_data", "");
			//处理数据信息
			if(sensorBean.getSensor_values().contains(";"))
			{
				JSONArray data_array = new JSONArray();
				System.out.println(sensorBean.getSensor_values());
				String[] s_valus = sensorBean.getSensor_values().split(";");
				for(String s : s_valus)
				{
					JSONObject s_object = new JSONObject();
					if(s.contains("|"))
					{
						String[] unit_symbol = s.split("\\|");
						s_object.put("unit", unit_symbol[0]);
						s_object.put("symbol", unit_symbol[1]);
					}
					else s_object.put("unit", s) ;
					data_array.add(s_object);
				}
				jsonObject.put("sensor_values", data_array);
			}
			else jsonObject.put("sensor_values", "");
		return jsonObject.toString();
	}
	public int sensorUpdate(int user_id, SensorBean sensorBean) {
		SensorDao sensorDao = this.sqlSessionTemplate.getMapper(SensorDao.class);
		return sensorDao.sensorUpdate(user_id, sensorBean);
	}
	public int sensorDelete(int user_id, int device_id, int sensor_id) {
		SensorDao sensorDao = this.sqlSessionTemplate.getMapper(SensorDao.class);
		return sensorDao.sensorDelete(user_id, device_id, sensor_id);
	}

}

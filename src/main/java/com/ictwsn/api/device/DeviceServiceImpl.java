package com.ictwsn.api.device;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ictwsn.bean.DeviceBean;
import com.ictwsn.utils.BaseDao;
import com.ictwsn.utils.Tools;

@Service
public class DeviceServiceImpl extends BaseDao implements DeviceService{
	
	public static Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);


	public int deleteDevice(int user_id,int device_id) {
		DeviceDao deviceDao = this.sqlSessionTemplate.getMapper(DeviceDao.class);
		return deviceDao.deleteDevice(user_id,device_id);
	}

	public String getDevice(int user_id,int device_id) {
		DeviceDao deviceDao = this.sqlSessionTemplate.getMapper(DeviceDao.class);
		DeviceBean deviceBean = deviceDao.getDevice(user_id,device_id);
		JSONObject jsonObject = JSONObject.fromObject(deviceBean);
		String[] s = deviceBean.getDevice_location().split("\\|");
		JSONObject j_unit = new JSONObject();
		for(String ss :s )
		{
			String[] unit = ss.split(":");
			j_unit.put(unit[0], unit[1]);
		}
		jsonObject.put("device_created",Tools.dateToString(deviceBean.getDevice_created()));
		jsonObject.put("device_location", j_unit);
		return jsonObject.toString();
	}

	public String listDevices(int user_id) {
		DeviceDao deviceDao = this.sqlSessionTemplate.getMapper(DeviceDao.class);
		List<DeviceBean> list = deviceDao.listDevices(user_id);
		JSONArray jsonArray = JSONArray.fromObject(list);
		for(int i=0;i<list.size();i++)
		{
			jsonArray.getJSONObject(i).put("device_created",Tools.dateToString(list.get(i).getDevice_created()));
			String[] s = list.get(i).getDevice_location().split("\\|");
			JSONObject j_unit = new JSONObject();
			for(String ss :s )
			{
				String[] unit = ss.split(":");
				j_unit.put(unit[0], unit[1]);
			}
			jsonArray.getJSONObject(i).put("device_location", j_unit);
		}
		return jsonArray.toString();
	}

	public int createDevice(DeviceBean deviceBean) {
		DeviceDao deviceDao = this.sqlSessionTemplate.getMapper(DeviceDao.class);
		deviceDao.createDevice(deviceBean);
		return deviceDao.getLastDeviceId(deviceBean.getUser_id());
	}

	public int updateDevice(DeviceBean deviceBean) {
		DeviceDao deviceDao = this.sqlSessionTemplate.getMapper(DeviceDao.class);
		return deviceDao.updateDevice(deviceBean);
	}

	public boolean checkDeviceId(int user_id, int device_id) {
		DeviceDao deviceDao = this.sqlSessionTemplate.getMapper(DeviceDao.class);
		System.out.println(user_id+"   "+device_id);
		if(deviceDao.checkDeviceId(user_id,device_id)>0) return true;
		else return false;
	}

}

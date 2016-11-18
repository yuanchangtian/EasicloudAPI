package com.ictwsn.device.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ictwsn.bean.DeviceBean;
import com.ictwsn.device.dao.DeviceManageDao;
import com.ictwsn.utils.BaseDao;
import com.ictwsn.utils.Page;

@Service
public class DeviceManageServiceImpl extends BaseDao implements DeviceManageService{
	
	public static Logger logger = LoggerFactory.getLogger(DeviceManageServiceImpl.class);
	
	public DeviceManageDao init()
	{
		DeviceManageDao deviceDao = this.sqlSessionTemplate.getMapper(DeviceManageDao.class);
		return deviceDao;
	}

	public int getDevicesCount(int user_id) {
		return this.init().getDevicesCount(user_id);
	}

	public List<DeviceBean> showDevices(int user_id, Page page) {
		return this.init().showDevices(user_id, page);
	}

	public int addDevice(DeviceBean deviceBean) {
		return this.init().addDevice(deviceBean);
	}

	public int updateDevice(DeviceBean deviceBean) {
		return this.init().updateDevice(deviceBean);
	}

	public int deleteDevice(int device_id, int user_id) {
		return this.init().deleteDevice(device_id, user_id);
	}
}

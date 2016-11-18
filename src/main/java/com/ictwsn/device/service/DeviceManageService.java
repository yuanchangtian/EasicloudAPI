package com.ictwsn.device.service;

import java.util.List;

import com.ictwsn.bean.DeviceBean;
import com.ictwsn.utils.Page;

public interface DeviceManageService {
	
	public int getDevicesCount(int user_id);
	public List<DeviceBean> showDevices(int user_id,Page page);
	public int addDevice(DeviceBean deviceBean);
	public int updateDevice(DeviceBean deviceBean);
	public int deleteDevice(int device_id,int user_id);
	
}

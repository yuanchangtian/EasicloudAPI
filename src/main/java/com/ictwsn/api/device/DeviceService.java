package com.ictwsn.api.device;

import com.ictwsn.bean.DeviceBean;

public interface DeviceService {
	public int createDevice(DeviceBean deviceBean);
	public int updateDevice(DeviceBean deviceBean);
	public int deleteDevice(int user_id,int device_id);
	public String getDevice(int user_id,int device_id);
	public String listDevices(int user_id);
	public boolean checkDeviceId(int user_id,int device_id);
}

package com.ictwsn.api.device;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ictwsn.bean.DeviceBean;

public interface DeviceDao {
		//创建设备
		@Insert("insert into tbl_device(user_id,device_active,device_name,device_created,device_description,device_publicity,device_location) "
		+"values(#{user_id},#{device_active},#{device_name},#{device_created},#{device_description},#{device_publicity},#{device_location})")
		public void createDevice(DeviceBean deviceBean);
		
	//搜寻该用户是否有此设备id
	@Select("select count(*) from tbl_device where user_id=#{0} and device_id=#{1}")
	public int checkDeviceId(int user_id,int device_id);
	
	//得到设备的id
	@Select("select device_id from tbl_device where user_id =#{user_id} order by device_id desc limit 0,1")
	public int getLastDeviceId(int user_id);
	
	//得到所有设备信息
	@Select("select * from tbl_device where user_id=#{user_id}")
	public List<DeviceBean> listDevices(int user_id);
	
	//查询具体设备
	@Select("select * from tbl_device where user_id=#{0} and device_id=#{1}")
	public DeviceBean getDevice(int user_id,int device_id);
	
	//删除具体设备
	@Select("delete from tbl_device where user_id=#{0} and device_id=#{1}")
	public int deleteDevice(int user_id,int device_id);
	
	//修改设备信息
	@Update("update tbl_device set device_active=#{device_active},device_name=#{device_name},device_created=#{device_created},device_description=#{device_description},device_publicity=#{device_publicity},device_location=#{device_location}"+
	" where user_id=#{user_id} and device_id=#{device_id}")
	public int updateDevice(DeviceBean deviceBean);
	
}

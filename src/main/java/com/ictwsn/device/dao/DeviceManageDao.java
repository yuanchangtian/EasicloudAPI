package com.ictwsn.device.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ictwsn.bean.DeviceBean;
import com.ictwsn.utils.Page;

public interface DeviceManageDao {
	//得到该用户所有的设备数量
	@Select("select count(*) from tbl_device where user_id = #{user_id}")
	public int getDevicesCount(int user_id);
	
	//分页查询所有该用户下的设备
	@Select("select * from tbl_device where user_id = #{user_id} ORDER BY device_id DESC limit #{page.beginIndex},#{page.everyPage}")
	public List<DeviceBean> showDevices(@Param("user_id")int user_id,@Param("page")Page page);
	
	//更新设备
	@Update("update tbl_device set device_name = #{device_name},"
			+ "device_active=#{device_active},device_description=#{device_description},"
			+ "device_publicity=#{device_publicity},device_location=#{device_location} where user_id = #{user_id} and device_id = ${device_id}")
	public int updateDevice(DeviceBean deviceBean);
	
	//添加设备
	@Insert("insert into tbl_device (user_id,device_active,device_name,device_created,device_description,"
			+ "device_publicity,device_location) values (#{user_id},#{device_active},#{device_name},"
			+ "#{device_created},#{device_description},#{device_publicity},#{device_location})")
	public int addDevice(DeviceBean deviceBean);
	
	//删除设备
	@Delete("delete from tbl_device where device_id=#{0} and user_id=#{1}")
	public int deleteDevice(int device_id, int user_id);
}

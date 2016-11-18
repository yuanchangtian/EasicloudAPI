package com.ictwsn.device.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ictwsn.bean.DeviceBean;
import com.ictwsn.bean.UserBean;
import com.ictwsn.device.service.DeviceManageService;
import com.ictwsn.utils.Page;
import com.ictwsn.utils.Tools;

@Controller
public class DeviceManageAction {
	
	public static Logger logger = LoggerFactory.getLogger(DeviceManageAction.class);
	
	@Resource DeviceManageService deviceService;
	
	@RequestMapping(value="/showdevices",method=RequestMethod.GET)
	public String showDevices(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currentPage",required=false)String currentPageStr) throws SQLException, IOException
	{
		UserBean user = Tools.getSessionUserBean(request);
		if(user==null) return "redirect:/login";
		int user_id = user.getUser_id();
		//查询指定页数
		Page page = Tools.getPage(1,deviceService.getDevicesCount(user_id),currentPageStr);
		List<DeviceBean> listDevices = deviceService.showDevices(user_id,page);
		//设置到request中
		request.setAttribute("page", page);
		request.setAttribute("list", listDevices);
		logger.info("设备页面跳转"+listDevices.toString());
		return "mydevice";
	}
	
	@RequestMapping(value="/deletedevice",method=RequestMethod.GET)
	public String deleteDevice(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currentPage",required=true)String currentPageStr,
			@RequestParam(value="device_id",required=true)int device_id) throws SQLException, IOException
	{
		UserBean user = Tools.getSessionUserBean(request);
		int user_id = user.getUser_id();
		int i = deviceService.deleteDevice(device_id, user_id);
		logger.info("更新删除的设备数量为：{}",i);
		return "redirect:/showdevices?currentPage="+currentPageStr;
	}
	
	@RequestMapping(value="/updatedevice",method=RequestMethod.POST)
	public String updateDevice(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currentPage",required=false)String currentPageStr,
			@RequestParam(value="device_id",required=true)int device_id,
			@RequestParam(value="device_active",required=false)String device_active,
			@RequestParam(value="device_name",required=false)String device_name,
			@RequestParam(value="device_description",required=false)String device_description,
			@RequestParam(value="device_location",required=false)String device_location,
			@RequestParam(value="device_publicity",required=false)String device_publicity) throws SQLException, IOException
		{
			//需要先得到userBean
			UserBean user = Tools.getSessionUserBean(request);
			int user_id = user.getUser_id();
				//执行更新操作
				DeviceBean deviceBean = new DeviceBean();
				deviceBean.setDevice_id(device_id);
				deviceBean.setDevice_active(Integer.valueOf(device_active));
				deviceBean.setDevice_description(device_description);
				deviceBean.setDevice_location(device_location);
				deviceBean.setDevice_name(device_name);
				deviceBean.setUser_id(user_id);
				deviceBean.setDevice_publicity(Integer.valueOf(device_publicity));
				//更新数据库
				int i = deviceService.updateDevice(deviceBean);
				logger.info("更新成功的设备数量为：{}",i);
			return "redirect:/showdevices?currentPage="+currentPageStr;
		}
	
	@RequestMapping(value="/adddevice",method=RequestMethod.POST)
	public String addDevice(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currentPage",required=false)String currentPageStr,
			@RequestParam(value="device_id",required=true)String device_id,
			@RequestParam(value="device_active",required=false)String device_active,
			@RequestParam(value="device_name",required=false)String device_name,
			@RequestParam(value="device_description",required=false)String device_description,
			@RequestParam(value="device_location",required=false)String device_location,
			@RequestParam(value="device_publicity",required=false)String device_publicity) throws SQLException, IOException
		{
			//需要先得到userBean
			UserBean user = Tools.getSessionUserBean(request);
			int user_id = user.getUser_id();
				//执行添加操作
				DeviceBean deviceBean = new DeviceBean();
				deviceBean.setDevice_active(Integer.valueOf(device_active));
				deviceBean.setDevice_description(device_description);
				deviceBean.setDevice_location(device_location);
				deviceBean.setDevice_name(device_name);
				deviceBean.setUser_id(user_id);
				deviceBean.setDevice_publicity(Integer.valueOf(device_publicity));
				//添加设备创建时间
				deviceBean.setDevice_created(new Date());
				//添加到数据库
				int i = deviceService.addDevice(deviceBean);
				logger.info("更新添加的设备数量为：{}",i);
			return "redirect:/showdevices?currentPage="+currentPageStr;
		}
}

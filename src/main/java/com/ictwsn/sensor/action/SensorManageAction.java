package com.ictwsn.sensor.action;

import java.io.IOException;
import java.sql.SQLException;
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

import com.ictwsn.bean.SensorBean;
import com.ictwsn.bean.UserBean;
import com.ictwsn.sensor.service.SensorManageService;
import com.ictwsn.utils.Page;
import com.ictwsn.utils.Tools;

@Controller
public class SensorManageAction {
	
	public static Logger logger = LoggerFactory.getLogger(SensorManageAction.class);
	
	@Resource SensorManageService sensorService;
	
	@RequestMapping(value="/showsensors",method=RequestMethod.GET)
	public String showSensors(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currentPage",required=false)String currentPageStr) throws SQLException, IOException
	{
		UserBean user = Tools.getSessionUserBean(request);
		if(user==null) return "redirect:/login";
		int user_id = user.getUser_id();
		//查询指定页数
		Page page = Tools.getPage(2,sensorService.geSensorsCount(user_id),currentPageStr);
		List<SensorBean> listSensors = sensorService.showSensors(user_id,page);
		//设置到request中
		request.setAttribute("page", page);
		request.setAttribute("list", listSensors);
		logger.info("传感器页面跳转"+listSensors.toString());
		return "mysensor";
	}
	
	@RequestMapping(value="/updatesensor",method=RequestMethod.POST)
	public String updateSensor(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currentPage",required=false)String currentPageStr,
			@RequestParam(value="device_id",required=false)String device_id,
			@RequestParam(value="sensor_data",required=false)String sensor_data,
			@RequestParam(value="sensor_values",required=false)String sensor_values,
			@RequestParam(value="sensor_name",required=false)String sensor_name,
			@RequestParam(value="sensor_imgurl",required=false)String sensor_imgurl,
			@RequestParam(value="sensor_description",required=false)String sensor_description,
			@RequestParam(value="sensor_id",required=false)String sensor_id
			) throws SQLException, IOException
	{
		UserBean user = Tools.getSessionUserBean(request);
		if(user==null) return "redirect:/login";
		int user_id = user.getUser_id();
		
		SensorBean sensorBean = new SensorBean();
		sensorBean.setSensor_id(Integer.valueOf(sensor_id));
		sensorBean.setDevice_id(Integer.valueOf(device_id));
		sensorBean.setSensor_data(sensor_data);
		sensorBean.setSensor_values(sensor_values);
		sensorBean.setSensor_imgurl(sensor_imgurl);
		sensorBean.setSensor_description(sensor_description);
		sensorBean.setSensor_name(sensor_name);
		sensorBean.setSensor_description(sensor_description);
		//调用service层
		int i = sensorService.updateSensor(sensorBean);
		logger.info("更新成功的数量为："+i);
		return "redirect:/showsensors?=currentPage"+currentPageStr;
	}
	
	@RequestMapping(value="/addsensor",method=RequestMethod.POST)
	public String addSensor(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currentPage",required=false)String currentPageStr,
			@RequestParam(value="device_id",required=false)String device_id,
			@RequestParam(value="sensor_data",required=false)String sensor_data,
			@RequestParam(value="sensor_values",required=false)String sensor_values,
			@RequestParam(value="sensor_name",required=false)String sensor_name,
			@RequestParam(value="sensor_imgurl",required=false)String sensor_imgurl,
			@RequestParam(value="sensor_description",required=false)String sensor_description,
			@RequestParam(value="sensor_id",required=false)String sensor_id
			) throws SQLException, IOException
	{
		UserBean user = Tools.getSessionUserBean(request);
		if(user==null) return "redirect:/login";
		int user_id = user.getUser_id();
		
		SensorBean sensorBean = new SensorBean();
		sensorBean.setDevice_id(Integer.valueOf(device_id));
		sensorBean.setSensor_data(sensor_data);
		sensorBean.setSensor_values(sensor_values);
		sensorBean.setSensor_imgurl(sensor_imgurl);
		sensorBean.setSensor_description(sensor_description);
		sensorBean.setSensor_name(sensor_name);
		sensorBean.setSensor_description(sensor_description);
		//调用service层
		int i = sensorService.insertSensor(sensorBean);
		logger.info("添加成功的数量为："+i);
		return "redirect:/showsensors?=currentPage"+currentPageStr;
	}
	
	
	@RequestMapping(value="/deletesensor",method=RequestMethod.GET)
	public String deleteSensor(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="sensor_id",required=false)int sensor_id,
			@RequestParam(value="device_id",required=false)int device_id,
			@RequestParam(value="currentPage",required=false)String currentPageStr
			) throws SQLException, IOException
	{
		//调用service层
		int i = sensorService.deleteSensor(sensor_id, device_id);
		logger.info("删除成功的数量为："+i);
		return "redirect:/showsensors?=currentPage"+currentPageStr;
	}
	
	
	
}

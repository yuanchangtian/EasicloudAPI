package com.ictwsn.analysis.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ictwsn.analysis.service.DataAnalysisService;
import com.ictwsn.bean.SensorBean;
import com.ictwsn.bean.UserBean;
import com.ictwsn.utils.Tools;

@Controller
public class DataAnalysisAction {
	
	public static Logger logger = LoggerFactory.getLogger(DataAnalysisAction.class);
	
	@Resource DataAnalysisService dataAnalysisService;
	
	
	//Ajax加载
	@RequestMapping(value="/monitoringData",method=RequestMethod.GET)
	public String monitoringData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="sensor_id",required=false)String sensor_id,
			@RequestParam(value="value_type",required=false)String value_type) throws SQLException, IOException
	{
		UserBean user = Tools.getSessionUserBean(request);
		if(user==null) return "redirect:/login";
		//查询到该用户的id
		int user_id = user.getUser_id();
		//若未指定sensor_id和value_type，则根据第一个值默认查找
		if(sensor_id==null&&value_type==null)
		{
			//通过用户查询到该用户的第一个传感器
			SensorBean firstSensorBean = dataAnalysisService.getFirstSensorId(user_id);
			System.out.println(firstSensorBean.toString());
			//取出该传感器的数据类型
			String sensor_values = firstSensorBean.getSensor_values();
			String split[] = sensor_values.split(";");
			String sensor_type[] = split[0].split("\\|");
			//取出类型名称和单位
			String sensor_type_name = sensor_type[0];
			System.out.println("sensor_type_name="+sensor_type_name);
			String sensor_type_unit = sensor_type[1];
			
			//这里查询24小时内的数据
			JSONArray jsonArray = dataAnalysisService.get24HoursDatapoints(user_id,
					firstSensorBean.getSensor_id(), sensor_type_name);
			JSONObject jsonContent = new JSONObject();
			jsonContent.put("data",jsonArray);
			JSONArray jsonUnits = new JSONArray();
			for(String s : split)
				jsonUnits.add(s);
			
			jsonContent.put("unit", jsonUnits);
			
			responseToJSON(response,jsonContent.toString());
		}
		return null;
	}
	//JSON封装返回数据
		public void responseToJSON(HttpServletResponse response,String jsonContent)
		{
			response.setContentType("text/html;charset=utf-8");
			try {
				PrintWriter out = response.getWriter();
				out.write(jsonContent);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	
}

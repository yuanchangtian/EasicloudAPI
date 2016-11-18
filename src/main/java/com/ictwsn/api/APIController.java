package com.ictwsn.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ictwsn.api.Algorithm.algorithmAPI;
import com.ictwsn.api.Algorithm.jgpml.ArrayToMatrix;
import com.ictwsn.api.Algorithm.jgpml.GaussianProcess;
import com.ictwsn.api.Algorithm.jgpml.covariancefunctions.CovLINone;
import com.ictwsn.api.Algorithm.jgpml.covariancefunctions.CovNoise;
import com.ictwsn.api.Algorithm.jgpml.covariancefunctions.CovSum;
import com.ictwsn.api.Algorithm.jgpml.covariancefunctions.CovarianceFunction;
import com.ictwsn.api.datapoint.DatapointService;
import com.ictwsn.api.device.DeviceService;
import com.ictwsn.api.sensor.SensorService;
import com.ictwsn.bean.DeviceBean;
import com.ictwsn.bean.SensorBean;
import com.ictwsn.utils.Tools;
import com.ictwsn.utils.redis.RedisOperation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import Jama.Matrix;

@Controller
@RequestMapping("/v1")
public class APIController {

	public static Logger logger = LoggerFactory.getLogger(APIController.class);

	private final String DEVICE_NAME = "device";
	private final String SENSOR = "sensor";

	@Resource
	DeviceService deviceService;
	@Resource
	SensorService sensorService;
	@Resource
	RedisOperation redisoperation;
	@Resource
	DatapointService datapointService;

	/**
	 * 设备类操作
	 * 
	 */

	// 罗列所有设备
	@RequestMapping(value = "/go", method = RequestMethod.GET)
	public String go(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("哈哈哈");
		return "index";
	}

	// 罗列所有设备
	@RequestMapping(value = "/devices", method = RequestMethod.GET)
	public String listDevices(HttpServletRequest request, HttpServletResponse response) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		responseToJSON(response, deviceService.listDevices(user_id));
		return null;
	}

	// 查询具体设备
	@RequestMapping(value = "/device/{device_id}", method = RequestMethod.GET)
	public String deviceShow(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		responseToJSON(response, deviceService.getDevice(user_id, device_id));
		return null;
	}

	// 创建具体设备
	@RequestMapping(value = "/device", method = RequestMethod.POST, headers = { "content-type=application/json" })
	@ResponseBody
	public String deviceCreate(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String jsonString) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		// 将JSON数据转化为Bean
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Date datetime = Tools.stringToDate(jsonObject.getString("device_created"));
		DeviceBean deviceBean = (DeviceBean) JSONObject.toBean(jsonObject, DeviceBean.class);

		deviceBean.setUser_id(user_id);
		deviceBean.setDevice_created(datetime);
		int device_id = deviceService.createDevice(deviceBean);
		// 返回创建后的传感器sensor_id
		jsonObject = new JSONObject();
		jsonObject.put("device_id", device_id);
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	// 修改具体设备
	@RequestMapping(value = "/device/{device_id}", method = RequestMethod.PUT, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String deviceUpdate(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@RequestBody String jsonString) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Date datetime = Tools.stringToDate(jsonObject.getString("device_created"));
		DeviceBean deviceBean = (DeviceBean) JSONObject.toBean(jsonObject, DeviceBean.class);
		deviceBean.setDevice_created(datetime);
		deviceBean.setUser_id(user_id);
		deviceBean.setDevice_id(device_id);
		// 返回消息
		int i = deviceService.updateDevice(deviceBean);
		jsonObject = new JSONObject();
		jsonObject.put("已更新行数", i);
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	// 删除具体设备
	@RequestMapping(value = "/device/{device_id}", method = RequestMethod.DELETE)
	public String deviceDelete(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		// 返回消息
		int i = deviceService.deleteDevice(user_id, device_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("已删除行数", i);
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	/**
	 * 传感器类操作
	 * 
	 */
	// 罗列传感器
	@RequestMapping(value = "device/{device_id}/sensors", method = RequestMethod.GET)
	public String listSensors(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id) {
		// 检查user_key
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		responseToJSON(response, sensorService.listSensors(user_id, device_id));
		return null;
	}

	// 查询具体传感器
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}", method = RequestMethod.GET)
	public String sensorShow(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@PathVariable int sensor_id) {
		// 检查user_key
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		responseToJSON(response, sensorService.sensorShow(user_id, device_id, sensor_id));
		return null;
	}

	// 创建具体传感器
	@RequestMapping(value = "/device/{device_id}/sensor", method = RequestMethod.POST, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String sensorCreate(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@RequestBody String jsonString) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		// 检查user_id是否能够操作该传感器
		if (!deviceService.checkDeviceId(user_id, device_id)) {
			logger.error("此用户不拥有此类设备:user_id={},device_id={}", user_id, device_id);
			return null;
		}
		// 将传感器JSON串转化为Bean
		// 将JSON数据转化为Bean
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		SensorBean sensorBean = (SensorBean) JSONObject.toBean(jsonObject, SensorBean.class);
		sensorBean.setDevice_id(device_id);
		int sensor_id = sensorService.createSensor(sensorBean);
		// 返回创建后的传感器sensor_id
		response.setContentType("text/html;charset=utf-8");
		try {
			PrintWriter out = response.getWriter();
			jsonObject = new JSONObject();
			jsonObject.put("sensor_id", sensor_id);
			out.write(jsonObject.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 修改具体传感器
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}", method = RequestMethod.PUT, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String sensorUpdate(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@PathVariable int sensor_id, @RequestBody String jsonString) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		// 将传感器JSON串转化为Bean
		// 将JSON数据转化为Bean
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		SensorBean sensorBean = (SensorBean) JSONObject.toBean(jsonObject, SensorBean.class);
		sensorBean.setDevice_id(device_id);
		sensorBean.setSensor_id(sensor_id);
		// 返回消息
		int i = sensorService.sensorUpdate(user_id, sensorBean);
		jsonObject = new JSONObject();
		jsonObject.put("已更新行数", i);
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	// 删除具体传感器
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}", method = RequestMethod.DELETE)
	public String sensorDelete(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@PathVariable int sensor_id) {
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		int i = sensorService.sensorDelete(user_id, device_id, sensor_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("已删除行数", i);
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	/**
	 * 数据点操作
	 * 
	 */
	// 查询数据点
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}/datapoint/{timestamp}", method = RequestMethod.GET)
	public String datapointShow(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@PathVariable int sensor_id, @PathVariable String timestamp) {
		// 进行user_id判断
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		// 判断设备传感器是否具有关联性
		if (datapointService.judgeUserIdSensorId(user_id, device_id, sensor_id) > 0) {
			String jsonpoint = datapointService.datapointShow(user_id, sensor_id, Tools.changeToDate8(timestamp));
			responseToJSON(response, jsonpoint.toString());
		} else// 输入的信息有误
		{
			responseToJSON(response, "未找到[" + user_id + "]用户下的传感器id");
		}
		return null;
	}

	// 创建数据点
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}/datapoint", method = RequestMethod.POST, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String datapointInsert(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@PathVariable int sensor_id, @RequestBody String jsondatapoint) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		DBObject dbObject = packagingData(sensor_id, jsondatapoint);
		// 找到用户的Mongodb集合并插入
		datapointService.insertToMongodb(String.valueOf(user_id), dbObject);
		return null;
	}

	// 批量插入数据点
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}/datapoints", method = RequestMethod.POST, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String datapointBatchInsert(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int device_id, @PathVariable int sensor_id, @RequestBody String jsondatapoint) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		// 转化为数组
		List<DBObject> list_dbObject = packagingBatchData(sensor_id, jsondatapoint);

		datapointService.insertBatchToMongodb(String.valueOf(user_id), list_dbObject);
		return null;
	}

	// 修改数据点
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}/datapoint/{timestamp}", method = RequestMethod.PUT, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String datapointUpdate(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@PathVariable int sensor_id, @PathVariable String timestamp, @RequestBody String jsondatapoint) { // 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		DBObject dbObject = packagingData(sensor_id, jsondatapoint);
		datapointService.datapointUpdate(String.valueOf(user_id), Tools.changeToDate8(timestamp), dbObject);
		return null;
	}

	// 删除数据点
	@RequestMapping(value = "/device/{device_id}/sensor/{sensor_id}/datapoint/{timestamp}", method = RequestMethod.DELETE)
	public String datapointDelete(HttpServletRequest request, HttpServletResponse response, @PathVariable int device_id,
			@PathVariable int sensor_id, @PathVariable String timestamp) { // 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		} // 判断设备传感器是否具有关联性
		if (datapointService.judgeUserIdSensorId(user_id, device_id, sensor_id) > 0) {
			String jsonpoint = datapointService.datapointDelete(String.valueOf(user_id), sensor_id,
					Tools.changeToDate8(timestamp));
			responseToJSON(response, jsonpoint.toString());
		} else// 输入的信息有误
		{
			responseToJSON(response, "未找到[" + user_id + "]用户下的传感器id");
		}
		return null;
	}

	// 新增数据点操作，user_key+传感器类型号
	// 查询数据点
	@RequestMapping(value = "/sensor/{sensor_id}/datapoint/{object_id}", method = RequestMethod.GET)
	public String datapointSimpleShow(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int sensor_id, @PathVariable String object_id) {
		// 进行user_id判断
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			return null;
		}
		// 判断设备传感器是否具有关联性
		if (datapointService.judgeUserIdSensorId(user_id, sensor_id) > 0) {
			String jsonpoint = datapointService.datapointShow(user_id, object_id, request.getHeader("object_id"));
			responseToJSON(response, jsonpoint.toString());
		} else// 输入的信息有误
		{
			responseToJSON(response, "未找到[" + user_id + "]用户下的传感器id[" + sensor_id + "]");
		}
		logger.info("我已执行完毕");
		return null;
	}

	// 创建数据点
	@RequestMapping(value = "/sensor/{sensor_id}/datapoint", method = RequestMethod.POST, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String datapointSimpleInsert(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int sensor_id, @RequestBody String jsondatapoint) {
		// 判断用户id
		int user_id = getUserId(request);
		System.out.println("user_id=" + user_id);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		// 插入数据点
		DBObject dbObject = packagingData(sensor_id, jsondatapoint);
		// 找到用户的Mongodb集合并插入
		datapointService.insertToMongodb(String.valueOf(user_id), dbObject);
		return null;
	}

	// 批量插入数据点
	@RequestMapping(value = "/sensor/{sensor_id}/datapoints", method = RequestMethod.POST, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String datapointSimpleBatchInsert(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int device_id, @PathVariable int sensor_id, @RequestBody String jsondatapoint) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		// 转化为数组
		List<DBObject> list_dbObject = packagingBatchData(sensor_id, jsondatapoint);
		datapointService.insertBatchToMongodb(String.valueOf(user_id), list_dbObject);
		return null;
	}

	// 修改数据点
	@RequestMapping(value = "/datapoint/{object_id}", method = RequestMethod.PUT, headers = {
			"content-type=application/json" })
	@ResponseBody
	public String datapointSimpleUpdate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int sensor_id, @PathVariable String object_id, @RequestBody String jsondatapoint) { // 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		DBObject dbObject = packagingData(sensor_id, jsondatapoint);
		datapointService.datapointUpdate(String.valueOf(user_id), object_id, dbObject);
		return null;
	}

	// 删除数据点
	@RequestMapping(value = "/datapoint/{object_id}", method = RequestMethod.DELETE)
	public String datapointSimpleDelete(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String object_id) { // 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		// 不用判断关联性，因为用户只能操作属于自己的collection
		String jsonpoint = datapointService.datapointDelete(String.valueOf(user_id), object_id);
		responseToJSON(response, jsonpoint.toString());
		return null;
	}

	/**
	 * 历史数据操作
	 */
	// 查询历史数据点
	@RequestMapping(value = "/sensor/{sensor_id}/datapoints/history", method = RequestMethod.GET)
	public String showHistoryDatapoints(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int sensor_id, @RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		System.out.println(user_id);
		String dataPoints = datapointService.getDatapoints(user_id, sensor_id, Tools.changeToDate8(start),
				Tools.changeToDate8(end), request.getHeader("object_id"));
		System.out.println(dataPoints);
		responseToJSON(response, dataPoints);
		return null;
	}

	// 条件查询某个传感器下数据点总数
	@RequestMapping(value = "/sensor/{sensor_id}/datapoints/count", method = RequestMethod.GET)
	public String getDatapointsCount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int sensor_id, @RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		int count = datapointService.getDatapointsCount(user_id, sensor_id, Tools.changeToDate8(start),
				Tools.changeToDate8(end));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("datapoint_count", count);
		// 写入到客户端查询总数
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	// 查询用户数据点总数
	@RequestMapping(value = "/datapoints/count", method = RequestMethod.GET)
	public String getAllDatapointsCount(HttpServletRequest request, HttpServletResponse response) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("datapoint_count", datapointService.getAllDatapointsCount(user_id));
		// 写入到客户端查询总数
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	// 查询传感器历史信息
	@RequestMapping(value = "/device/{device_id}/sensors/information", method = RequestMethod.GET)
	public String getSensorsInformation(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int device_id) {
		String jsonSensors = "";
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			jsonSensors = "user_key不正确：" + request.getHeader("user_key");
			logger.error("user_key不正确：{}", request.getHeader("user_key"));
			responseToJSON(response, jsonSensors);
			return null;
		}
		/*
		 * //检查用户是否拥有该设备 if(!deviceService.checkDeviceId(user_id, device_id)) {
		 * jsonSensors = "用户"+user_id+"不拥有设备"+device_id;
		 * logger.info("用户{}不拥有设备{}",user_id,device_id);
		 * responseToJSON(response, jsonSensors); return null; }
		 */
		// 查找传感器
		jsonSensors = sensorService.getSensorsInformation(user_id, device_id, request.getHeader("object_id"));
		responseToJSON(response, jsonSensors);
		return null;
	}

	// 条件查询用户数据点
	@RequestMapping(value = "/sensor/{sensor_id}/datapoints/condition", method = RequestMethod.GET)
	public String getDatapointsCondition(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "exp", required = true) String exp,
			@RequestParam(value = "con", required = true) String con,
			@RequestParam(value = "val", required = true) String val, @PathVariable int sensor_id) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		// 条件查询
		JSONArray jsonArray = datapointService.getDatapointsCondition(con, exp, val, String.valueOf(user_id),
				request.getHeader("object_id"), sensor_id);
		if (jsonArray == null)
			responseToJSON(response, "各条件名的数量不一致");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("datacount", jsonArray.size());
		jsonObject.put("datapoints", jsonArray);
		// 写入到客户端查询总数
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	/**
	 * 算法接口
	 */
	@RequestMapping(value = "/sensor/{sensor_id}/datapoints/dataAnalysis", method = RequestMethod.GET)
	public String showAnalysisResult(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int sensor_id, @RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end,
			@RequestParam(value = "data_type", required = false) String data_type) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		System.out.println(user_id);
		String dataPoints = datapointService.getDatapoints(user_id, sensor_id, Tools.changeToDate8(start),
				Tools.changeToDate8(end), request.getHeader("object_id"));
		JSONArray jsonArray = null;
		try {
			jsonArray = JSONArray.fromObject(dataPoints);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		double[] nums = new double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("values");
			nums[i] = jsonArray2.getJSONObject(0).getDouble(data_type);
			System.out.println(nums[i]);

		}
		double maxValue = algorithmAPI.getMax(nums);
		double minValue = algorithmAPI.getMin(nums);
		double meanValue = algorithmAPI.getMean(nums);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("maxValue", maxValue);
		jsonObject.put("minValue", minValue);
		jsonObject.put("meanValue", meanValue);
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	@RequestMapping(value = "/sensor/{sensor_id}/datapoints/prediction", method = RequestMethod.GET)
	public String GPPrediction(HttpServletRequest request, HttpServletResponse response, @PathVariable int sensor_id,
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end,
			@RequestParam(value = "data_type", required = false) String data_type,
			@RequestParam(value = "exponent_number", required = false) String exponent_number,
			@RequestParam(value = "step_number", required = false) String step_number) {
		// 判断用户id
		int user_id = getUserId(request);
		if (user_id == -1) {
			logger.error("user_key不正确_：{}", request.getHeader("user_key"));
			return null;
		}
		System.out.println(user_id);
		String dataPoints = datapointService.getDatapoints(user_id, sensor_id, Tools.changeToDate8(start),
				Tools.changeToDate8(end), request.getHeader("object_id"));
		// responseToJSON(response, dataPoints);
		JSONArray jsonArray = null;
		try {
			jsonArray = JSONArray.fromObject(dataPoints);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		double[] nums = new double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("values");
			nums[i] = jsonArray2.getJSONObject(0).getDouble(data_type);
			System.out.println(nums[i]);

		}
		int len = nums.length;
		double[] res = new double[len / 100];
		int index = 1;
		for (int i = 0; i < res.length; i++) {
			ArrayList<Double> list = new ArrayList<Double>();
			for (int j = len - 1; j >= 0; j -= index) {
				list.add(nums[j]);
				if (list.size() >= 100)
					break;
			}
			int sublen = list.size();
			double[] temp = new double[sublen];
			for (int m = 0; m < sublen; m++) {
				temp[m] = list.get(sublen - m - 1);
			}
			CovarianceFunction covFunc = new CovSum(6, new CovLINone(), new CovNoise());
			GaussianProcess gp = new GaussianProcess(covFunc);
			double[][] logtheta0 = new double[][] { { 0.1 }, { Math.log(0.1) } };
			/*
			 * double[][] logtheta0 = new double[][]{ {0.1}, {0.2}, {0.3},
			 * {0.4}, {0.5}, {0.6}, {0.7}, {Math.log(0.1)}};
			 */

			Matrix params0 = new Matrix(logtheta0);
			// int exponent = Integer.valueOf(exponent_number);
			// int step = Integer.valueOf(step_number);
			int exponent = 4;
			int step = 1;
			System.out.println("hh");
			System.out.println(temp.length);
			Matrix[] data = ArrayToMatrix.load2(temp, exponent, step);
			Matrix X = data[0];
			Matrix Y = data[1];
			gp.train(X, Y, params0, -20);
			double[] predict_X = new double[exponent + 1];
			for (int n = 0; n < exponent; n++) {
				predict_X[n] = temp[temp.length - exponent + n];
			}
			predict_X[exponent] = 0;
			Matrix[] datastar = ArrayToMatrix.load2(predict_X, exponent, 1);
			Matrix Xstar = datastar[0];
			Matrix Ystar = datastar[1];
			Matrix[] result = gp.predict(Xstar);
			res[i] = result[0].get(0, 0);
			System.out.println(res[i]);
			result[0].print(result[0].getColumnDimension(), 3);
			result[1].print(result[1].getColumnDimension(), 3);
			index++;
		}
		for (int i = 0; i < res.length; i++)
			System.out.println(res[i]);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("history", jsonArray);
		jsonObject.put("predict_value", res);
		//System.out.println(jsonObject.toString());
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	@RequestMapping(value = "/sensor/{sensor_id}/datapoints/correlation", method = RequestMethod.GET)
	public String CorrelationAnalysis(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int sensor_id, @RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end) {
		// 判断用户id
		// int user_id = getUserId(request);
		// if (user_id == -1) {
		// logger.error("user_key不正确_：{}", request.getHeader("user_key"));
		// return null;
		// }
		// System.out.println(user_id);
		// String dataPoints = datapointService.getDatapoints(user_id,
		// sensor_id, Tools.changeToDate8(start),
		// Tools.changeToDate8(end), request.getHeader("object_id"));
		// responseToJSON(response, dataPoints);
		double[] nums1 = { 3, 2, 3, 4, 5, 5, 7, 7, 9 };// temperature
		double[] nums2 = { 19, 15, 11, 12, 10, 14, 12, 15, 17 };// light
		double pearsons = algorithmAPI.getPearsonsCorrelation(nums1, nums2);
		double spearmans = algorithmAPI.getSpearmansCorrelation(nums1, nums2);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pearsons", pearsons);
		jsonObject.put("spearmans", spearmans);
		responseToJSON(response, jsonObject.toString());
		return null;
	}

	// 返回User_id
	public int getUserId(HttpServletRequest request) {
		String user_key = request.getHeader("user_key");
		int i = 0;
		if (user_key == null || user_key.equals(""))
			return -1;
		try {
			i = redisoperation.get(user_key, Integer.class);
		} catch (Exception e) {
			logger.error("user_key不正确：{}", user_key);
			e.printStackTrace();
			return -1;
		}
		return i;
	}

	// 存入Mongodb前的封装操作
	public DBObject packagingData(int sensor_id, String jsondatapoint) {
		// 转化为JSON字符串
		JSONObject jsonObject = JSONObject.fromObject(jsondatapoint);
		// 存入传感器类型id
		jsonObject.put("sensor_id", sensor_id);
		DBObject dbObject = (BasicDBObject) JSON.parse(jsonObject.toString());
		// 放入timestamp
		Date date = null;
		String timestamp = jsonObject.getString("timestamp");
		if (timestamp == null || timestamp.equals("") || timestamp.equals("null"))
			date = Tools.getNowDate8();
		date = Tools.changeToDate8(timestamp);
		dbObject.put("timestamp", date);
		return dbObject;
	}

	// 批量插入时存入Mongodb前的封装操作
	public List<DBObject> packagingBatchData(int sensor_id, String jsondatapoint) { // 转化为JSON字符串
		JSONArray jsonArray = JSONArray.fromObject(jsondatapoint);
		JSONObject jsonObject = new JSONObject();
		List<DBObject> list = new ArrayList<DBObject>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			// 存入传感器类型id
			jsonObject.put("sensor_id", sensor_id);
			DBObject dbObject = (BasicDBObject) JSON.parse(jsonObject.toString());
			// 放入timestamp
			Date date = null;
			String timestamp = jsonObject.getString("timestamp");
			if (timestamp.equals("") || timestamp == null)
				date = Tools.getNowDate8();
			date = Tools.changeToDate8(timestamp);
			dbObject.put("timestamp", date);
			list.add(dbObject);
		}
		return list;
	}

	// JSON封装返回数据
	public void responseToJSON(HttpServletResponse response, String jsonContent) {
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
	// 局部异常类处理
	/*
	 * @ExceptionHandler public ModelAndView exceptionHandler(Exception ex){
	 * ModelAndView mv = new ModelAndView("error"); mv.addObject("exception",
	 * ex); logger.error("异常信息：", ex.toString()); return mv; }
	 */
}

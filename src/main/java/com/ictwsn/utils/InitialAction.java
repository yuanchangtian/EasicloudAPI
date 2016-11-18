package com.ictwsn.utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class InitialAction implements
		ApplicationListener<ContextRefreshedEvent> {// ContextRefreshedEvent为初始化完毕事件，spring还有很多事件可以利用

	// @Autowired
	// private IRoleDao roleDao;

	/**
	 * 当一个ApplicationContext被初始化或刷新触发
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
//		InsertSimulateData();
		System.out.println("spring容易初始化完毕================================================");
	}
	
	public static void InsertSimulateData()
	{

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		while(true)
		{
				httpPost = new HttpPost(
						"http://localhost:8080/EasiCloudAPI/v1/sensor/1/datapoint");
		
				StringEntity requestEntity = new StringEntity(generatedJSONObject(),"utf-8");
				requestEntity.setContentEncoding("UTF-8");
				httpPost.setHeader("Content-type", "application/json");
				httpPost.setHeader("user_key","5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023");
				httpPost.setEntity(requestEntity);
		
				try {
					response = httpClient.execute(httpPost);
					System.out.println(response.getStatusLine());
					
					 HttpEntity entity = response.getEntity();
					 EntityUtils.consume(entity);
					//每2秒插入一次
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					 
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
//			response.close();
//			httpClient.close();
	}

	public static String generatedJSONObject() {
		Random random = new Random();// 定义随机类
		int sensor_node = random.nextInt(1000) + 1;// 返回[0,10)集合中的整数，注意不包括10
		DecimalFormat df = new DecimalFormat(".###");// 保留小数
		double min, max;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sensor_node", sensor_node);
		jsonObject.put("sensor_node", sensor_node);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		jsonObject.put("timestamp", nowTime);
		JSONArray jsonArray = new JSONArray();
		min = 10;
		max = 50;
		double temperature = min + ((max - min) * new Random().nextDouble());
		min = 30;
		max = 60;
		double humidity = min + ((max - min) * new Random().nextDouble());
		min = 500;
		max = 2000;
		double light = min + ((max - min) * new Random().nextDouble());
		JSONObject jsonValue = new JSONObject();
		jsonValue.put("temperature", df.format(temperature));
		jsonValue.put("humidity", df.format(humidity));
		jsonValue.put("light", df.format(light));
		jsonArray.add(jsonValue);
		jsonObject.put("values", jsonArray);
		return jsonObject.toString();
	}

	public static void main(String[] args) {
		InsertSimulateData();
	}

}

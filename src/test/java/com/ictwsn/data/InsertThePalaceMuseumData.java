package com.ictwsn.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.ictwsn.utils.Tools;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class InsertThePalaceMuseumData {
	public static void main(String[] args) {
		// 获取MySQL
		String sql = "select * from data";
		DbPoolConnection dbp = DbPoolConnection.getInstance();
		DruidPooledConnection con;

		// 获取Mongodb
		MongoClient mc = MongoDBJDBC.getInstance();
		// 连接到数据库
		DB mongoDatabase = mc.getDB("test");
		System.out.println(new Date().toString() + ":获取Mongodb连接");
		Date date = null;
		try {
			con = dbp.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int i = 0;
			List<DBObject> list = new ArrayList<DBObject>();
			while (rs.next() && i < 10000) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("sensor_id", 1);
				jsonObject.put("sensor_node", rs.getInt("originAddr"));
				JSONArray jsonarray = new JSONArray();
				JSONObject json_data = new JSONObject();
				json_data.put("temperature", rs.getDouble("data1"));
				json_data.put("humidity", rs.getDouble("data0"));
				json_data.put("light", rs.getDouble("data2"));
				jsonarray.add(json_data);
				jsonObject.put("values", jsonarray);
				DBObject dbObject = (BasicDBObject) JSON.parse(jsonObject
						.toString());
				// 放入timestamp
				String timestamp = rs.getString("collectTime");
				if (timestamp.equals("") || timestamp == null)
					date = Tools.getNowDate8();
				date = Tools.changeToDate8(timestamp);
				dbObject.put("timestamp", date);
				list.add(dbObject);
				System.out.println("当前存入数据条数为：  " + i + "  "
						+ dbObject.toString());
				i++;
			}
			// 插入mongodb
			System.out.println("开始插入mongodb：" + new Date().toString());
			mongoDatabase.getCollection("10001").insert(list);
			System.out.println("插入完成：" + new Date().toString());

			// 关闭MySQL连接
			ps.close();
			con.close();
			// 关闭MongoDB连接
			mc.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

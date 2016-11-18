package com.ictwsn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ictwsn.utils.JDBCBean;

public class Test {
	
	public static void main(String[] args) {
		
		JDBCBean jdbcbean = new JDBCBean();
		Connection connection = jdbcbean.getConnection();
		String sql = "select * from ggdata";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery(sql);
			
			//JSON转化
//			List<JSONObject> list = new ArrayList<JSONObject>();
			int i = 0;
			while(rs.next())
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("timestamp", rs.getString("collecttime"));
				jsonObject.put("originAddr", rs.getInt("originaddr"));
				jsonObject.put("seqNo", rs.getLong("seqno"));
				JSONArray jsonArray = new JSONArray();
				JSONObject j_data = new JSONObject();
				j_data.put("light", Double.valueOf(rs.getFloat("light")+""));
				j_data.put("temperature", Double.valueOf(rs.getFloat("temperature")+""));
				j_data.put("humidity", Double.valueOf(rs.getFloat("humidity")+""));
				j_data.put("correctHumidity1", Double.valueOf(rs.getFloat("correcthumidity1")+""));
				j_data.put("correctHumidity2", Double.valueOf(rs.getFloat("correcthumidity2")+""));
				jsonArray.add(j_data);
				jsonObject.put("values", jsonArray);
				sendPost("http://10.22.0.78:8081/EasiCloudAPI/v1/device/10010/sensor/2/datapoint",jsonObject.toString());
				System.out.println(i++);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				list.add(jsonObject);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*String[] s = "sjkkl;asd;".split(";");
		System.out.println(s[1]);*/
	}
	
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("user_key", "qylFnRyJ1oC5JK3e32zTtw==");
            conn.setRequestProperty("Content-Type", "application/json");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	
}

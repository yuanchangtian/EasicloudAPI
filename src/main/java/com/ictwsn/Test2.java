package com.ictwsn;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test2 {
	
	public static void main(String[] args) {
		/*try {
			System.out.println(Tools.EncoderByMd5("huangruoran"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		
		/*String random_code = "";
		for(int i=0;i<6;i++)
		{
			random_code += (int)(Math.random()*10);
		}
		System.out.println(random_code);*/
		
		
		/*JSONArray jsonArray1 = new JSONArray();
		jsonArray1.add(20);
		jsonArray1.add(50);
		JSONArray jsonArray2 = new JSONArray();
		jsonArray2.add(30);
		jsonArray2.add(40.3);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonArray1);
		jsonArray.add(jsonArray2);
		System.out.println(jsonArray.toString());*/
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date date = new Date(Long.parseLong("1478342749000"));
		System.out.println(date.toString());
		
		/*try {
			String userKey = Tools.EncrypSHA("huangruoran"+System.currentTimeMillis()+"huangruoran");
			System.out.println(userKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		*/
	}
}

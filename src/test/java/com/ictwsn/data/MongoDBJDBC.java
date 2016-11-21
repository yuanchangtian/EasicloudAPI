package com.ictwsn.data;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBJDBC {

	private static MongoClient mongoClient = null;
	private static List<ServerAddress> addrs = null;
	private static List<MongoCredential> credentials = null;

	static {
		// 连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
		// ServerAddress()两个参数分别为 服务器地址 和 端口
		ServerAddress serverAddress;
		try {
			serverAddress = new ServerAddress("localhost", 27017);
			addrs = new ArrayList<ServerAddress>();
			addrs.add(serverAddress);
			// MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
			MongoCredential credential = MongoCredential
					.createScramSha1Credential("ruo", "test",
							"123456".toCharArray());
			credentials = new ArrayList<MongoCredential>();
			credentials.add(credential);
			
			/*
			 * 没有用户名密码时将static整个给注释掉，并将下面的mongoClient = new MongoClient(addrs, credentials);
			 * 给替换成mongoClient = new MongoClient( "localhost" , 27017 );即可
	        */
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(new Date().toString()+":初始化MongoDB完毕");
	}

	public static synchronized MongoClient getInstance() {
		if (null == mongoClient) {
			// 通过连接认证获取MongoDB连接
			mongoClient = new MongoClient(addrs, credentials);
		}
		return mongoClient;
	}


	public static void main(String[] args) {
		MongoClient mc = MongoDBJDBC.getInstance();
		// 连接到数据库
		DB mongoDatabase = mc.getDB("test");
		System.out.println(mongoDatabase.getCollectionNames());
	}

}
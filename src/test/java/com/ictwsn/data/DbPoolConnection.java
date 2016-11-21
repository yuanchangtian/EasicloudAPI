package com.ictwsn.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

public class DbPoolConnection {
	private static DbPoolConnection databasePool = null;
	private static DruidDataSource dds = null;
	static {
		Properties properties = loadPropertyFile("db_server.properties");
		try {
			dds = (DruidDataSource) DruidDataSourceFactory
					.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(new Date().toString()+":初始化MySQL完毕");
	}

	private DbPoolConnection() {
	}

	public static synchronized DbPoolConnection getInstance() {
		if (null == databasePool) {
			databasePool = new DbPoolConnection();
		}
		System.out.println(new Date().toString()+":获取MySQL连接池");
		return databasePool;
	}

	public DruidPooledConnection getConnection() throws SQLException {
		return dds.getConnection();
	}

	public static Properties loadPropertyFile(String fullFile) {
		String webRootPath = null;
		if (null == fullFile || fullFile.equals(""))
			throw new IllegalArgumentException(
					"Properties file path can not be null : " + fullFile);
		webRootPath = DbPoolConnection.class.getClassLoader().getResource("")
				.getPath();
		// webRootPath = new File(webRootPath).getParent();
		InputStream inputStream = null;
		Properties p = null;
		try {
			inputStream = new FileInputStream(new File(webRootPath
					+ File.separator + fullFile));
			p = new Properties();
			p.load(inputStream);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: "
					+ fullFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Properties file can not be loading: " + fullFile);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	public static void main(String[] args) {
		String sql = "select count(*) from data";
		DbPoolConnection dbp = DbPoolConnection.getInstance();
		DruidPooledConnection con;
		try {
			con = dbp.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) System.out.print(rs.getInt(1));
//			ps.executeUpdate();
//			ps.close();
//			con.close();
//			dbp = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
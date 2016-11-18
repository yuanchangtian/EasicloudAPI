package com.ictwsn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

public class JDBCBean {
	private static Logger logger = Logger.getLogger(JDBCBean.class);
	
	private String driver = "";
	private String conn = "";
	private String DBname ="";
	private String DBpwd = "";
	private Connection connection = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public JDBCBean()
	{
		/*
//		InputStream is = JDBCBean.class.getResourceAsStream("../configuration.properties");
//		Properties p = new Properties();
		try {
//			p.load(is);
			driver = p.getProperty("database.driver");
			conn = p.getProperty("database.conn");
			DBname = p.getProperty("database.username");
			DBpwd = p.getProperty("database.password");
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		driver = "com.mysql.jdbc.Driver";
		conn = "jdbc:mysql://localhost:3306/easicloud?useUnicode=true&amp;characterEncoding=UTF-8";
		DBname = "root";
		DBpwd = "root";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error("数据库驱动异常");
			e.printStackTrace();
		}
	}
	
	public Connection getConnection()
	{
		try {
			connection = DriverManager.getConnection(conn,DBname,DBpwd);
		} catch (SQLException e) {
			logger.error("数据库连接异常");
			e.printStackTrace();
		}
		return connection;
	}
	
	public Statement createStatement()
	{
		try {
			stmt = getConnection().createStatement();
		} catch (SQLException e) {
			logger.error("创建语句对象异常");
			e.printStackTrace();
		}
		return stmt;
	}
	public ResultSet executeQuery(String sql)
	{
		try {
			rs = createStatement().executeQuery(sql);
		} catch (SQLException e) {
			logger.error("执行查询操作异常");
			e.printStackTrace();
		}
		return rs;
	}
	public int executeUpdate(String sql)
	{
		int result = 0;
		try {
			result = createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			logger.error("执行更新操作异常");
			e.printStackTrace();
		}
		return result;
	}
	
	public void close()
	{
		try{
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(connection!=null)
				connection.close();
		}catch(SQLException e)
		{
			logger.error("执行关闭操作异常");
			e.printStackTrace();
		}	
	}
	
	//全部关闭
	public void closeAll(Connection conn,PreparedStatement pstmt,ResultSet rs){
		if (rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("RS关闭发生异常");
				e.printStackTrace();
			}
		}
		if (pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				logger.error("pstmt关闭发生异常");
				e.printStackTrace();
			}
		}
		if (conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("conn关闭发生异常");
				e.printStackTrace();
			}
		}
	}

}



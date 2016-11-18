package com.ictwsn;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
	
		@Select("select * from user where username = #{username}")
	 	public User selectUser(User user); 
	 	
	 	@Insert("insert into user(username, password) values(#{username}, #{password})")  
	    public void insertUser(User user);  
	    /* public void updateUser(User user);  
	    public void deleteUser(int userId);*/  
}

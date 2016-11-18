package com.ictwsn.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.ictwsn.User;
import com.ictwsn.utils.redis.RedisOperation;
/** 
 * dao层测试例子 
 * 
 */  
public class TestDao extends JunitDaoBase {  
	
	@Resource RedisOperation redisOperation;
  
    @Test  
    public void testUserDao() {  
        User user = new User();  
        user.setPassword("123456");  
        System.out.println(user); 
        System.out.println(redisOperation.get("4QrcOUm6Wau+VuBX8g+IPg==",Integer.class));
    }  
}  

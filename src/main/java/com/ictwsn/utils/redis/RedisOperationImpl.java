package com.ictwsn.utils.redis;

/**
 * Redis存储对象
 * 对象转byte[]，然后利用spring-data-redis进行存储，
 * 所以save形式为key(String->byte[])-value(Object->byte[])，
 * get形式为key(byte[]->String)-value(byte[]->Object)
 * 
 */

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Repository;

import com.ictwsn.utils.BaseDao;


@Repository
public class RedisOperationImpl extends BaseDao implements RedisOperation{
	
	public void save(final String key, Object value) {
		final byte[] vbytes = SerializeUtil.serialize(value);       
		redisTemplate.execute(new RedisCallback<Object>() {           
			public Object doInRedis(RedisConnection connection)          
					throws DataAccessException {                
				connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
				return null;          
				}        
			}); 
	}


	
	public <T> T get(final String key, Class<T> elementType) {
		 return redisTemplate.execute(new RedisCallback<T>() {     
			 public T doInRedis(RedisConnection connection)   
					 throws DataAccessException {             
				 byte[] keybytes = key.getBytes();
				 if (connection.exists(keybytes)) {                
					 byte[] valuebytes = connection.get(keybytes);            
					 @SuppressWarnings("unchecked")                
					 T value = (T) SerializeUtil.unserialize(valuebytes);  
					 return value;          
					 }              
				 return null;         
				 }     
			 }); 
	}

	
	
}

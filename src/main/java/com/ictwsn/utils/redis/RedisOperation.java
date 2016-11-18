package com.ictwsn.utils.redis;


public interface RedisOperation {
	public void save(final String key, Object value);
	public <T> T get(final String key, Class<T> elementType);
}

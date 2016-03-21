/**
 * 
 */
package com.nutrisystem.orange.java.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.nutrisystem.orange.java.constant.AppConstant;

/**
 * @author Wei Gao
 * 
 */
public abstract class Cache {
	private String cacheKeyPrefix;

	private ValueOperations<String, Object> valueOperations;

	private RedisTemplate<String, Object> redisTemplate;

	protected abstract Object find(String id);

	public Object save(String id) {
		Object entity = find(id);
		if (entity != null) {
			try {
				valueOperations.set(cacheKeyPrefix + id, entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	public Object read(String id) {
		Object entity = null;
		try {
			entity = valueOperations.get(cacheKeyPrefix + id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (entity == null)
			entity = save(id);
		return entity;
	}

	public void delete(String id) {
		redisTemplate.delete(cacheKeyPrefix + id);
	}

	public void setEntityClassName(String entityClassName) {
		this.cacheKeyPrefix = AppConstant.NAMESPACE + entityClassName.toLowerCase() + ":";
	}

	public void setValueOperations(ValueOperations<String, Object> valueOperations) {
		this.valueOperations = valueOperations;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}

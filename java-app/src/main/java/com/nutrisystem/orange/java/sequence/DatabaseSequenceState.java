/**
 * 
 */
package com.nutrisystem.orange.java.sequence;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import org.springframework.jdbc.core.JdbcTemplate;

import com.nutrisystem.orange.java.ws.helper.Redis;

/**
 * Sequence id generator based on database sequencer.
 * 
 * @author Wei Gao
 * 
 */
public class DatabaseSequenceState extends AbstractSequenceState {
	private JdbcTemplate jdbcTemplate;

	private Object repository;

	private String findMaxIdMethod;

	private Redis redis;

	@Override
	public boolean isAlive() {
		return true;
	}

	@Override
	public Long nextVal(SequenceContext sequenceContext) {
		long seqId;
		if (redis.isAlive() && distributedCache.isHealthy()) {
			sequenceContext.setSequenceState(sequenceContext
					.getRedisSequenceState());
			sequenceContext.getRedisSequenceState().setCurrVal(
					sequenceContext.getDbSequenceState().getMaxId());
			seqId = sequenceContext.nextVal();
		} else {
			seqId = dbNextVal();
			long maxId = getMaxId();
			while (seqId <= maxId) {
				seqId = resetSequence(maxId);
				maxId = getMaxId();
			}
		}
		return seqId;
	}

	@Override
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName.replaceAll(":", "_");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (exists()) { // check the sequence value is consistent with maxId
			if (dbNextVal() != getMaxId())
				resetSequence(getMaxId());
		} else {
			createSequence();
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setRepository(Object repository) {
		this.repository = repository;
	}

	public void setFindMaxIdMethod(String findMaxIdMethod) {
		this.findMaxIdMethod = findMaxIdMethod;
	}

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	private void createSequence() {
		jdbcTemplate.execute("create sequence " + sequenceName
				+ " increment 1 start " + getMaxId());
		dbNextVal();
	}

	private long resetSequence(long start) {
		jdbcTemplate.execute("alter sequence " + sequenceName
				+ " restart with " + start);
		return dbNextVal();
	}

	/**
	 * test if this sequence exists in the database.
	 * 
	 * @return true/false
	 */

	private boolean exists() {
		return jdbcTemplate
				.queryForObject(
						"select count(*) = 1 from pg_statio_all_sequences where relname = ? ",
						new Object[] { sequenceName },
						new int[] { Types.VARCHAR }, Boolean.class);
	}

	private long dbNextVal() {
		return jdbcTemplate.queryForObject("select nextval(?)",
				new Object[] { sequenceName }, new int[] { Types.VARCHAR },
				Long.class);
	}

	public long getMaxId() {
		try {
			Long maxId = (Long) repository.getClass()
					.getMethod(findMaxIdMethod).invoke(repository);
			return maxId == null || maxId < 1 ? 1 : maxId;
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}

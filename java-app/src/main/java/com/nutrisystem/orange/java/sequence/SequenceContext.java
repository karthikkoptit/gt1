/**
 * 
 */
package com.nutrisystem.orange.java.sequence;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Wei Gao
 *
 */
public class SequenceContext implements InitializingBean{
    private AbstractSequenceState sequenceState;
    
    private RedisSequenceState redisSequenceState;
    
    private DatabaseSequenceState dbSequenceState;

    public Long nextVal() {
	return sequenceState.nextVal(this);
    }
    
    public void setSequenceState(AbstractSequenceState sequenceState) {
        this.sequenceState = sequenceState;
    }

    public RedisSequenceState getRedisSequenceState() {
        return redisSequenceState;
    }

    public void setRedisSequenceState(RedisSequenceState redisSequenceState) {
        this.redisSequenceState = redisSequenceState;
    }

    public DatabaseSequenceState getDbSequenceState() {
        return dbSequenceState;
    }

    public void setDbSequenceState(DatabaseSequenceState dbSequenceState) {
        this.dbSequenceState = dbSequenceState;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	if (redisSequenceState.isAlive())
	    this.sequenceState = redisSequenceState;
	else
	    this.sequenceState = dbSequenceState;
    }
}

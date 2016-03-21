/**
 * 
 */
package com.nutrisystem.orange.java.sequence;

import org.springframework.beans.factory.InitializingBean;

import com.nutrisystem.orange.java.cache.DistributedCache;


/**
 * @author Wei Gao
 * 
 */
public abstract class AbstractSequenceState implements InitializingBean {
    protected String sequenceName;
    
    protected DistributedCache distributedCache;

    public AbstractSequenceState() {
    }

    public abstract Long nextVal(SequenceContext sequenceContext);
    
    public abstract void setSequenceName(String sequenceName);
    
    public abstract boolean isAlive();

    public void setDistributedCache(DistributedCache distributedCache) {
        this.distributedCache = distributedCache;
    }
}

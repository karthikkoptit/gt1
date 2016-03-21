/**
 * 
 */
package com.nutrisystem.orange.java.sequence;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.data.redis.core.ValueOperations;

/**
 * Sequence id generator based on Redis cache.
 * 
 * @author Wei Gao
 * 
 */
public class RedisSequenceState extends AbstractSequenceState {
    private static final String FINGERPRINT_NAME = "j:fingerprint";

    @SuppressWarnings("rawtypes")
    private ValueOperations valueOperations;

    private DatabaseSequenceState dbSequenceState;

    public RedisSequenceState() {
    }

    @Override
    public void setSequenceName(String sequenceName) {
	this.sequenceName = sequenceName;
    }

    @Override
    public boolean isAlive() {
	try {
	    distributedCache.currVal(sequenceName);
	    redisCurrVal();
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	if (!validFingerprint())
	    throw new RuntimeException("fingerprint does not match.");
	Long maxId = dbSequenceState.getMaxId();
	distributedCache.updateSequence(sequenceName, maxId);
	setCurrVal(maxId);
    }

    @SuppressWarnings("unchecked")
    private void setFingerprint(String fingerprint) {
	try {
	    valueOperations.set(FINGERPRINT_NAME, fingerprint);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private boolean validFingerprint() {
	try {
	    String fingerprintInCache = (String) valueOperations.get(FINGERPRINT_NAME);
	    String fingerprint = generateFingerprint();
	    if (fingerprintInCache == null) {
		setFingerprint(fingerprint);
		return true;
	    }
	    else 
		return fingerprint.equals(fingerprintInCache);
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    private String generateFingerprint() {
	InetAddress host;
	try {
	    host = InetAddress.getByName(distributedCache.getDasHost());
	    MessageDigest md5 = MessageDigest.getInstance("MD5");
	    String fingerprintOrigin = host.getHostName() + host.getHostAddress();
	    return new String(md5.digest(fingerprintOrigin.getBytes()), "UTF-8");
	} catch (UnknownHostException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
	    throw new RuntimeException("cannot generate fingerprint.");
	}
    }

    @Override
    public Long nextVal(SequenceContext sequenceContext) {
	long seqId;
	try {
	    seqId = redisNextVal();
	    while (seqId <= distributedCache.currVal(sequenceName)) {
		Long maxId = dbSequenceState.getMaxId();
		distributedCache.updateSequence(sequenceName, maxId);
		setCurrVal(maxId);
		seqId = redisNextVal();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    sequenceContext.setSequenceState(dbSequenceState);
	    seqId = sequenceContext.nextVal();
	}
	return seqId;
    }

    @SuppressWarnings("unchecked")
    public boolean setCurrVal(Long seqId) {
	try {
	    valueOperations.set(sequenceName, String.valueOf(seqId));
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    @SuppressWarnings("rawtypes")
    public void setValueOperations(ValueOperations valueOperations) {
	this.valueOperations = valueOperations;
    }

    @SuppressWarnings("unchecked")
    private Long redisNextVal() {
	return valueOperations.increment(sequenceName, 1);
    }

    public Long redisCurrVal() {
	return (Long) valueOperations.get(sequenceName);
    }

    public void setDbSequenceState(DatabaseSequenceState dbSequenceState) {
	this.dbSequenceState = dbSequenceState;
    }
}

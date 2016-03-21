/**
 * 
 */
package com.nutrisystem.orange.java.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;

import com.nutrisystem.orange.java.entity.diyapp.TimeBucket;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;

/**
 * @author Wei Gao
 * 
 */
public class TimeBucketLookup {
	private TimeBucketRepository timeBucketRepository;

	private BidiMap<Integer, String> timeBucketMap;
	
	private Map<Integer, TimeBucket> timeBucketEntityMap;

	public TimeBucketLookup() {
	}

	public Integer getTimeBucketId(String TimeBucket) {
		return timeBucketMap.getKey(TimeBucket);
	}

	public boolean isValidTimeBucketId(Integer timeBucketId) {
		return timeBucketMap.get(timeBucketId) != null;
	}
	
	public String getTimeBucketStart(Integer timeBucketId) {
		return timeBucketEntityMap.get(timeBucketId).getTimeBucketStart();
	}
	
	public String getTimeBucketEnd(Integer timeBucketId) {
		return timeBucketEntityMap.get(timeBucketId).getTimeBucketEnd();
	}

	public void init() {
		List<TimeBucket> timeBucketList = timeBucketRepository.findAll();

		timeBucketMap = new DualHashBidiMap<Integer, String>();
		timeBucketEntityMap = new HashMap<>();

		for (TimeBucket timeBucket : timeBucketList) {
			timeBucketMap.put(timeBucket.getTimeBucketId(), timeBucket.getTimeBucket());
			timeBucketEntityMap.put(timeBucket.getTimeBucketId(), timeBucket);
		}
		timeBucketMap = UnmodifiableBidiMap.unmodifiableBidiMap(timeBucketMap);
	}

	public void setTimeBucketRepository(TimeBucketRepository timeBucketRepository) {
		this.timeBucketRepository = timeBucketRepository;
	}
}

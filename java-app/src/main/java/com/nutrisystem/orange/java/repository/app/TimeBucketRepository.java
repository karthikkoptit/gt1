/**
 * 
 */
package com.nutrisystem.orange.java.repository.app;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nutrisystem.orange.java.entity.diyapp.TimeBucket;

/**
 * @author Wei Gao
 *
 */
@PersistenceContext(name="persistence/app", unitName = "appPersistenceUnit")
public interface TimeBucketRepository extends JpaRepository<TimeBucket, Integer> {
	@Query("select coalesce(min(timeBucketId), 0) from TimeBucket")
	public int findMinTimeBucketId();
	
	@Query("select coalesce(max(timeBucketId), -1) from TimeBucket")
	public int findMaxTimeBucketId();
	
	public List<TimeBucket> findByTimeBucketStartLessThanEqualAndTimeBucketEndGreaterThanEqual(String time1, String time2);
}

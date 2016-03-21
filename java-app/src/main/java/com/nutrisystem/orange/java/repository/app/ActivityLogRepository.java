/**
 * 
 */
package com.nutrisystem.orange.java.repository.app;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;

/**
 * @author Wei Gao
 * 
 */
@PersistenceContext(name = "persistence/app", unitName = "appPersistenceUnit")
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    @Query("select coalesce(max(activityLogId), 0) from ActivityLog")
    public Long findMaxActivityLogId();

	public List<ActivityLog> findByUserIdAndActivityLogDateBetweenOrderByTimeBucketIdAsc(Integer userId,
	    String logDateStart, String logDateEnd);

    public List<ActivityLog> findByUserIdAndActivityLogDateBetweenAndTimeBucketIdInOrderByTimeBucketIdAsc(
	    Integer userId, String logDateStart, String logDateEnd, List<Integer> timeBucketIdList);

    public List<ActivityLog> findByUserIdAndActivityLogDateBetweenOrderByActivityLogDateAscTimeBucketIdAsc(
	    Integer userId, String logDateStart, String logDateEnd);

    public List<ActivityLog> findByUserIdAndActivityLogDateBetween(Integer userId, String logDateStart,
	    String logDateEnd);
}

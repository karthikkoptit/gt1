/**
 * 
 */
package com.nutrisystem.orange.java.repository.app;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nutrisystem.orange.java.entity.diyapp.FoodLog;

/**
 * @author Wei Gao
 * 
 */
@PersistenceContext(name = "persistence/app", unitName = "appPersistenceUnit")
public interface FoodLogRepository extends JpaRepository<FoodLog, Long> {
	@Query("select coalesce(max(foodLogId), 0) from FoodLog")
	public Long findMaxFoodLogId();

	public List<FoodLog> findByUserIdAndFoodLogDateBetween(Integer userId, String logDateStart, String logDateEnd);

	public List<FoodLog> findByUserIdAndFoodLogDateBetweenAndTimeBucketIdIn(Integer userId, String logDateStart,
			String logDateEnd, List<Integer> timeBucketIdList);

	public List<FoodLog> findByUserIdAndFoodLogDateBetweenOrderByFoodLogDateAscTimeBucketIdAsc(Integer userId,
			String logDateStart, String logDateEnd);
}

/**
 * 
 */
package com.nutrisystem.orange.java.repository.app;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nutrisystem.orange.java.entity.diyapp.UserProfileHistory;

/**
 * @author Wei Gao
 * 
 */
@PersistenceContext(name = "persistence/app", unitName = "appPersistenceUnit")
public interface UserProfileHistoryRepository extends JpaRepository<UserProfileHistory, Long> {
    @Query("select coalesce(max(userProfileHistoryId), 0) from UserProfileHistory")
    public Long findMaxUserProfileHistoryId();
    
    @Query(value = "select count(*) > 0 from user_profile_history u where u.user_id = ?1", nativeQuery = true)
        public boolean hasUser(Integer userId);
    
    public List<UserProfileHistory> findByUserIdOrderByUserProfileHistoryIdAsc(Integer userId);
    
    public List<UserProfileHistory> findByUserIdOrderByUserProfileHistoryIdDesc(Integer userId);
}

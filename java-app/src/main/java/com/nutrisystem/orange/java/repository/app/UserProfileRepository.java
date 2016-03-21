/**
 * 
 */
package com.nutrisystem.orange.java.repository.app;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nutrisystem.orange.java.entity.diyapp.UserProfile;

/**
 * @author Wei Gao
 * 
 */
@PersistenceContext(name = "persistence/app", unitName = "appPersistenceUnit")
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("select coalesce(max(userProfileId), 0) from UserProfile")
    public Long findMaxUserProfileId();
    
    public List<UserProfile> findByUserId(Integer userId);

    @Query(value = "select count(*) > 0 from user_profile u where u.user_id = ?1", nativeQuery = true)
    public boolean hasUser(Integer userId);
}

/**
 * 
 */
package com.nutrisystem.orange.java.cache;

import java.util.List;

import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.repository.app.UserProfileRepository;

/**
 * @author Wei Gao
 *
 */
public class UserProfileCache extends Cache {
    private UserProfileRepository userProfileRepository;
    
    /* (non-Javadoc)
     * @see com.nutrisystem.orange.java.cache.Cache#find(java.lang.String)
     */
    @Override
    protected Object find(String id) {
	List<UserProfile> userProfiles = userProfileRepository.findByUserId(Integer.parseInt(id));
	return userProfiles == null || userProfiles.size() == 0 ? null : userProfiles.get(0);
    }

    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
}

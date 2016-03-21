/**
 * 
 */
package com.nutrisystem.orange.java.appservice;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.repository.app.UserProfileRepository;
import com.nutrisystem.orange.java.util.EntityUtil;

/**
 * @author Wei Gao
 * 
 */
@Component
public class UserProfileAppService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Transactional
    public void update(UserProfile inputUserProfile) {
	UserProfile userProfile = userProfileRepository.findByUserId(inputUserProfile.getUserId()).get(0);
	inputUserProfile.setUserId(null);
	
	EntityUtil.update(new BeanWrapperImpl(userProfile), new BeanWrapperImpl(inputUserProfile));	
    }
}

/**
 * 
 */
package com.nutrisystem.orange.java.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nutrisystem.orange.java.entity.diyapp.Diet;
import com.nutrisystem.orange.java.repository.app.DietRepository;

/**
 * @author Wei Gao
 * 
 */
public class DietLookup {
    private DietRepository dietRepository;

    private Map<String, Integer> dietMap;

    public DietLookup() {
    }

    public Integer getDietId(String diet) {
	return dietMap.get(diet);
    }

    public void setDietRepository(DietRepository dietRepository) {
	this.dietRepository = dietRepository;
    }
    
    public void init() {
	List<Diet> dietList = dietRepository.findAll();

	dietMap = new HashMap<>();
	for (Diet diet : dietList) {
	    dietMap.put(diet.getDiet(), diet.getDietId());
	}
    }
}

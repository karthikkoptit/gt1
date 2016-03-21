/**
 * 
 */
package com.nutrisystem.orange.java.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nutrisystem.orange.java.entity.diyapp.Radius;
import com.nutrisystem.orange.java.repository.app.RadiusRepository;

/**
 * @author Wei Gao
 * 
 */
public class RadiusLookup {
    private RadiusRepository radiusRepository;

    private Map<Integer, Float> radiusMap;

    public RadiusLookup() {
    }

    public float getRadius(Integer radiusId) {
	return radiusMap.get(radiusId);
    }

    public void setRadiusRepository(RadiusRepository radiusRepository) {
	this.radiusRepository = radiusRepository;
    }
    
    public void init() {
	List<Radius> radiusList = radiusRepository.findAll();

	radiusMap = new HashMap<>();
	for (Radius radius : radiusList) {
	    radiusMap.put(radius.getRadiusId(), radius.getRadiusMax());
	}
    }
}

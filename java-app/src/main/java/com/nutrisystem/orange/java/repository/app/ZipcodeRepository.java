/**
 * 
 */
package com.nutrisystem.orange.java.repository.app;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nutrisystem.orange.java.entity.diyapp.Zipcode;

/**
 * @author Wei Gao
 * 
 */
@PersistenceContext(name = "persistence/app", unitName = "appPersistenceUnit")
public interface ZipcodeRepository extends JpaRepository<Zipcode, String> {
}

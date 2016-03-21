/**
 * 
 */
package com.nutrisystem.orange.java.repository.fdb;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nutrisystem.orange.java.entity.diyfdb.CustomActivity;

/**
 * @author Wei Gao
 * 
 */
@PersistenceContext(name = "persistence/fdb", unitName = "fdbPersistenceUnit")
public interface CustomActivityRepository extends JpaRepository<CustomActivity, Integer> {
}

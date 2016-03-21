/**
 * 
 */
package com.nutrisystem.orange.java.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanWrapper;

import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * @author Wei Gao
 * 
 */
public class EntityUtil {
	private static final String LASTUPDATETIME_FIELD = "lastUpdateTime";

	public static boolean equal(BeanWrapper e1, BeanWrapper e2) {
		if (e1 == null || e2 == null)
			return false;
		if (!e1.getWrappedClass().getName()
				.equals(e2.getWrappedClass().getName()))
			return false;

		for (String attribute : attributeNames(e1.getWrappedInstance())) {
			if (attribute.equals(LASTUPDATETIME_FIELD))
				continue;
			else if (e1.getPropertyTypeDescriptor(attribute).getAnnotation(
					javax.persistence.Id.class) != null)
				continue;
			else if (e1.getPropertyValue(attribute) == null
					&& e2.getPropertyValue(attribute) != null)
				return false;
			else if (e1.getPropertyValue(attribute) == null
					&& e2.getPropertyValue(attribute) == null)
				continue;
			else if (!e1.getPropertyValue(attribute).equals(
					e2.getPropertyValue(attribute))) {
				return false;
			}
		}
		return true;
	}

	public static void update(BeanWrapper e1, BeanWrapper e2) {
		if (e1 == null || e2 == null)
			throw new IllegalArgumentException("cannot be null.");
		if (!e1.getWrappedClass().getName()
				.equals(e2.getWrappedClass().getName()))
			throw new IllegalArgumentException(
					"type of entities are different.");

		boolean updated = false;
		List<String> attributeList = attributeNames(e2.getWrappedInstance());
		for (String attribute : attributeList) {
			if (attribute.equals(LASTUPDATETIME_FIELD))
				continue;
			else if (e1.getPropertyTypeDescriptor(attribute).getAnnotation(
					javax.persistence.Id.class) != null)
				continue;
			else {
				Object value = e2.getPropertyValue(attribute);
				if (value != null) {
					updated = true;
					e1.setPropertyValue(attribute, value);
				}
			}
		}
		if (updated && attributeList.contains(LASTUPDATETIME_FIELD)) {
			e1.setPropertyValue(LASTUPDATETIME_FIELD,
					DateUtil.getCurrentTimeStamp());
		}
	}

	private static List<String> attributeNames(Object entity) {
		Field[] fields = entity.getClass().getDeclaredFields();
		List<String> attributeNameList = new ArrayList<String>();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())
					&& !Modifier.isStatic(field.getModifiers())) {
				attributeNameList.add(field.getName());
			}
		}
		return attributeNameList;
	}

	private EntityUtil() {
	}
}

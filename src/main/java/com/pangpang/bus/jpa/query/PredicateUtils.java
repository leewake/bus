package com.pangpang.bus.jpa.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;


/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午3:40:00 
*/
public class PredicateUtils {

	public static <T> Predicate build(Root<?> root, CriteriaBuilder cb, T params) {
		List<Predicate> predicates = new ArrayList<>();
		//通过反射拿到查询参数中所有值
		Class<?> clazz = params.getClass();
		List<Field> fields = FieldUtils.getAllFieldsList(clazz);
		for (Field field : fields) {
			Method method = getMethod(clazz, field);
			if (method == null) {
				continue;
			}
			
			buildPredicate(root, cb, predicates, clazz, field, ReflectionUtils.invokeMethod(method, params));
		}
		
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	private static <T> void buildPredicate(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, Class<?> clazz,
			Field field, T value) {
		EntityProperty entityProperty = field.getAnnotation(EntityProperty.class);
		if (entityProperty == null) {
			equal(root, cb, predicates, field.getName(), value);
		} else {
			if (entityProperty.ignore()) {
				return ;
			}
			
			String propertyName = StringUtils.hasLength(entityProperty.propertyName()) ? entityProperty.propertyName() : field.getName();
			switch (entityProperty.operation()) {
			case eq:
				equal(root, cb, predicates, propertyName, value);
				break;

			default:
				break;
			}
		}
		
	}

	private static <T> void equal(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = equal(root, cb, field, value);
		
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	/**
	 * 需要处理字符串的情况
	 * @param root
	 * @param cb
	 * @param field
	 * @param value
	 * @return
	 */
	private static <T> Predicate equal(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null; 
		if (value != null) {
			if (value instanceof String) {
				if (!((String) value).isEmpty()) {
					predicate = cb.equal(root.get(field), value);
				}
			} else {
				predicate = cb.equal(root.get(field), value);
			}
		}
		return predicate;
	}

	private static Method getMethod(Class<?> clazz, Field field) {
		String methodName = "get" + StringUtils.capitalize(field.getName());
		Method method = ReflectionUtils.findMethod(clazz, methodName);
		if (method == null && (field.getType() == boolean.class || field.getType() == Boolean.class)) {
			methodName = "is" + StringUtils.capitalize(field.getName());
			method = ReflectionUtils.findMethod(clazz, methodName);
		}
		return method;
	}

}

package com.pangpang.bus.jpa.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.pangpang.bus.common.util.DateUtils;




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
			Class<?> propertyType = entityProperty.propertyType();
			String propertyName = StringUtils.hasLength(entityProperty.propertyName()) ? entityProperty.propertyName() : field.getName();
			switch (entityProperty.operation()) {
			case eq:
				equal(root, cb, predicates, propertyName, value);
				break;
			case notEq:
				notEqual(root, cb, predicates, propertyName, value);
				break;
			case like:
				like(root, cb, predicates, propertyName, (String) value);
				break;
			case likePrefix:
				likePrefix(root, cb, predicates, propertyName, (String) value);
				break;
			case isNull:
				isNull(root, cb, predicates, propertyName);
				break;
			case isNullOrEq:
				isNullOrEq(root, cb, predicates, propertyName, value);
				break;
			case isNotNull:
				isNotNull(root, cb, predicates, propertyName);
				break;
			case existOrNot:
				existOrNot(root, cb, predicates, propertyName, value);
				break;
			case lessThan:
				lessThan(root, cb, predicates, propertyName, castToComparable(propertyType, value));
				break;
			case lessThanOrEqualTo:
				lessThanOrEqualTo(root, cb, predicates, propertyName, castToComparable(propertyType, value));
				break;
			case greaterThan:
				greaterThan(root, cb, predicates, propertyName, castToComparable(propertyType, value));
				break;
			case greaterThanOrEqualTo:
				greaterThanOrEqualTo(root, cb, predicates, propertyName, castToComparable(propertyType, value));
				break;
			case in:
				in(root, cb, predicates, propertyName, (List<?>) value);
				break;
			default:
				equal(root, cb, predicates, propertyName, value);
				break;
			}
		}
		
	}

	private static <T> void existOrNot(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = existOrNot(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	private static <T> Predicate existOrNot(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null;
		if (value != null) {
			if (value instanceof String) {
				if (!((String) value).isEmpty()) {
					if (((String) value).equalsIgnoreCase("Y")) {
						predicate = cb.isNotNull(root.get(field));
					} else {
						predicate = cb.isNull(root.get(field));
					}
				}
			} else if (value instanceof Boolean) {
				if ((Boolean) value) {
					predicate = cb.isNotNull(root.get(field));
				} else {
					predicate = cb.isNull(root.get(field));
				}
			}
		}
		return predicate;
	}

	private static <T> void in(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, List<T> value) {
		Predicate predicate = in(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	private static <T> Predicate in(Root<?> root, CriteriaBuilder cb, String field, List<T> value) {
		Predicate predicate = null;
		if (!CollectionUtils.isEmpty(value)) {
			Iterator<T> iterator = value.iterator();
			CriteriaBuilder.In<T> in = cb.in(root.get(field));
			while (iterator.hasNext()) {
				in.value(iterator.next());
			}
			return in;
		}
		return predicate;
	}

	private static <T> void greaterThanOrEqualTo(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = greaterThanOrEqualTo(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> Predicate greaterThanOrEqualTo(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null;
		if (value != null) {
			if (value instanceof String) {
				if (!((String) value).isEmpty()) {
					predicate = cb.greaterThanOrEqualTo(root.<Comparable>get(field), (Comparable) value);
				}
			} else {
				predicate = cb.greaterThanOrEqualTo(root.<Comparable>get(field), (Comparable) value);
			}
		} 
		return predicate;
	}

	private static <T> void lessThanOrEqualTo(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = lessThanOrEqualTo(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> Predicate lessThanOrEqualTo(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null;
		if (value != null) {
			if (value instanceof String) {
				if (!((String) value).isEmpty()) {
					predicate = cb.lessThanOrEqualTo(root.<Comparable>get(field), (Comparable) value);
				} 
			} else {
				predicate = cb.lessThanOrEqualTo(root.<Comparable>get(field), (Comparable) value);
			}
		}
		return predicate;
	}

	private static <T> void greaterThan(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = greaterThan(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> Predicate greaterThan(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null;
		if (value != null) {
			if (value instanceof String) {
				if (!((String) value).isEmpty()) {
					predicate = cb.greaterThan(root.<Comparable>get(field), (Comparable) value);
				}
			} else {
				predicate = cb.greaterThan(root.<Comparable>get(field), (Comparable) value);
			}
		}
		return predicate;
	}

	private static <T> void lessThan(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = lessThan(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	/**
	 * 此处不应该只写Date类型
	 * 只要实现Comparable接口的类都可以进行比较
	 * @param root
	 * @param cb
	 * @param field
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> Predicate lessThan(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null;
		if (value != null) {
			if (value instanceof String) {
				if (!(((String) value).isEmpty())) {
					predicate = cb.lessThan(root.<Comparable>get(field), (Comparable) value);
				}
			} else {
				predicate = cb.lessThan(root.<Comparable>get(field), (Comparable) value);
			}
		} 
		return predicate;
	}

	private static <T> void isNullOrEq(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = isNullOrEq(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	private static <T> Predicate isNullOrEq(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null;
		if (value != null) {
			if (value instanceof String) {
				if (!(((String) value).isEmpty())) {
					predicate = cb.or(cb.isNull(root.get(field)), cb.equal(root.get(field), value));
				}
			} else {
				predicate = cb.or(cb.isNull(root.get(field)), cb.equal(root.get(field), value));
			}
		}
		return predicate;
	}

	private static void isNotNull(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field) {
		Predicate predicate = isNotNull(root, cb, field);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	private static Predicate isNotNull(Root<?> root, CriteriaBuilder cb, String field) {
		return cb.isNotNull(root.get(field));
	}

	private static <T> void isNull(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field) {
		Predicate predicate = isNull(root, cb, field);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	private static <T> Predicate isNull(Root<?> root, CriteriaBuilder cb, String field) {
		return cb.isNull(root.get(field));
	}

	private static void likePrefix(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, String value) {
		Predicate predicate = likePrefix(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}
	
	private static Predicate likePrefix(Root<?> root, CriteriaBuilder cb, String field, String value) {
		Predicate predicate = null; 
		if (value != null && !value.isEmpty()) {
			predicate = cb.like(root.get(field), "%" + field);
		}
		return predicate;
	}

	private static <T> void like(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, String value) {
		Predicate predicate = like(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}
	
	private static Predicate like(Root<?> root, CriteriaBuilder cb, String field, String value) {
		Predicate predicate = null; 
		if (value != null && !value.isEmpty()) {
			predicate = cb.like(root.get(field), "%" + field + "%");
		}
		return predicate;
	}
	
	private static <T> void equal(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = equal(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}

	/**
	 * 需要处理字符串的情况,排除""情况
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
	
	private static <T> void notEqual(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String field, T value) {
		Predicate predicate = notEqual(root, cb, field, value);
		if (predicate != null) {
			predicates.add(predicate);
		}
	}
	
	private static <T> Predicate notEqual(Root<?> root, CriteriaBuilder cb, String field, T value) {
		Predicate predicate = null;
		if (value != null) {
			if (value instanceof String) {
				if (!((String) value).isEmpty()) {
					predicate = cb.notEqual(root.get(field), value);
				}
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
	
	private static <T> Comparable<?> castToComparable(Class<?> propertyType, T value) {
		if (value == null) {
			return null;
		}
		if (propertyType != Object.class && propertyType != value.getClass()) {
			if (propertyType == Date.class) {
				Date dateValue = DateUtils.parse(DateUtils.PATTERN_SIMPLE, value.toString());
				if (dateValue == null)
					throw new RuntimeException("invalid date value: " + value);
				return Comparable.class.cast(dateValue);
			}
			throw new RuntimeException("invalid type for value: " + value);
		} else {
			return Comparable.class.cast(value);
		}
	}

}

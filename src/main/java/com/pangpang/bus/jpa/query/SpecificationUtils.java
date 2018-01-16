package com.pangpang.bus.jpa.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;


/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午3:29:49 
*/
public class SpecificationUtils {
	
	public static <T,P> Specification<T> build(final P params){
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return PredicateUtils.build(root, cb, params);
			}
		};
	}

}

package com.pangpang.bus.jpa.query;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.pangpang.bus.common.QueryOperation;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午3:02:51 
*/

@Retention(RUNTIME)
@Target(FIELD)
public @interface EntityProperty {
	
	String propertyName() default "";
	
	QueryOperation operation() default QueryOperation.eq;
	
	Class<?> propertyType() default Object.class;
	
	boolean ignore() default false;
	
}

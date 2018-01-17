package com.pangpang.bus.common.param;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.pangpang.bus.common.OrderStatus;
import com.pangpang.bus.common.QueryOperation;
import com.pangpang.bus.jpa.query.EntityProperty;

import lombok.Data;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午7:56:07 
*/

@Data
public class OrderSearchParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@EntityProperty(propertyType=Date.class, operation=QueryOperation.greaterThan)
	private String createTime;
	
	@EntityProperty(operation=QueryOperation.existOrNot)
	private boolean test;
	
	private OrderStatus orderStatus;
	
	@EntityProperty(propertyName="orderStatus", operation=QueryOperation.notEq)
	private OrderStatus excludeStatus = OrderStatus.CANCEL;
	
	@EntityProperty(operation=QueryOperation.in)
	private List<String> supplierName;

}

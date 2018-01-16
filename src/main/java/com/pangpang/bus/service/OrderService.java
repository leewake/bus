package com.pangpang.bus.service;

import java.util.List;

import com.pangpang.bus.common.param.OrderSearchParam;
import com.pangpang.bus.jpa.entity.OrderItemEntity;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午7:55:14 
*/
public interface OrderService {

	List<OrderItemEntity> searchOrder(OrderSearchParam params);
}

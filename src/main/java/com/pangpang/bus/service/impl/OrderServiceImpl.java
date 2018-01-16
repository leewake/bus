package com.pangpang.bus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pangpang.bus.common.param.OrderSearchParam;
import com.pangpang.bus.jpa.entity.OrderItemEntity;
import com.pangpang.bus.jpa.query.SpecificationUtils;
import com.pangpang.bus.jpa.repository.OrderRepository;
import com.pangpang.bus.service.OrderService;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午7:57:32 
*/

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<OrderItemEntity> searchOrder(OrderSearchParam params) {
		List<OrderItemEntity> orders = orderRepository.findAll(SpecificationUtils.build(params));
		return orders;
	}

}

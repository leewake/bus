package com.pangpang.bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.bus.common.param.OrderSearchParam;
import com.pangpang.bus.jpa.entity.OrderItemEntity;
import com.pangpang.bus.service.OrderService;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午7:50:33 
*/

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/search")
	public List<OrderItemEntity> search(@RequestBody OrderSearchParam params){	
		return orderService.searchOrder(params);
	}
	
}

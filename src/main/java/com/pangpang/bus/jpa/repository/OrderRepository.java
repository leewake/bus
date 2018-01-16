package com.pangpang.bus.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pangpang.bus.jpa.entity.OrderItemEntity;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午8:00:27 
*/

@Repository
public interface OrderRepository extends JpaSpecificationExecutor<OrderItemEntity>, JpaRepository<OrderItemEntity, Long>{

}

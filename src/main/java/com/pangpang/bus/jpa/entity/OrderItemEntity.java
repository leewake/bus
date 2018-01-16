package com.pangpang.bus.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午7:41:19 
*/

@Data
@Entity
@ToString
@Table(name = "order_item")
public class OrderItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name = "order_name")
	private String orderName;
	
	@Column(name = "contact")
	private String contact;
	
	@Column(name = "price")
	private Long price;
	
	@Column(name = "supplier_name")
	private String supplierName;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "visit_time")
	private Date visitTime;
	
	@Column(name = "is_test")
	private Boolean isTest;
	
}

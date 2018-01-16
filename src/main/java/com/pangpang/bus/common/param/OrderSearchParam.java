package com.pangpang.bus.common.param;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/** 
* @author  : lijingwei
* @version ：2018年1月15日 下午7:56:07 
*/

@Data
public class OrderSearchParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Date createTime;
	
	private boolean test;

}

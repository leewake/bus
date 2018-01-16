package com.pangpang.bus.common;

import lombok.AllArgsConstructor;

/** 
* @author  : lijingwei
* @version ：2018年1月16日 上午9:59:59 
*/

@AllArgsConstructor
public enum OrderStatus {
	
	NORMAL("正常"),
	CANCEL("取消");
	
	private String cnName;
	
	public String getCnName(){
		return this.cnName;
	}
	
}

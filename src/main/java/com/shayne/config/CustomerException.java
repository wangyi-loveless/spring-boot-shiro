package com.shayne.config;

/**
 * 统一异常处理
 * @Author WY
 * @Date 2017年12月27日
 */
public class CustomerException extends Exception {

	private static final long serialVersionUID = 7105031511408543821L;

	public CustomerException(String msg){
		super(msg);
	}
}

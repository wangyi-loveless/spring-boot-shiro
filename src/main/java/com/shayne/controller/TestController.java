package com.shayne.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器基类
 * @Author 王小张
 * @Date 2020年8月19日 上午11:05:05
 */
@RestController
@RequestMapping(value = "test")
public class TestController extends BaseController{

	/**
	 * 测试方法
	 * @return
	 */
	@RequestMapping
	public String test() {
		return "SUCCESS";
	}
	
}

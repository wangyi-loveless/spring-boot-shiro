package com.shayne.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shayne.constans.ApiCons;

/**
 * 字典类型和字典管理
 * @Author 王小张
 * @Date 2020年8月25日 下午5:31:21
 */
@RestController
@RequestMapping(value=ApiCons.API + ApiCons.SEPARATOR + ApiCons.DICTIONARY)
public class DictionaryController extends BaseController {

    
}

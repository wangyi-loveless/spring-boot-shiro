package com.shayne.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shayne.constans.ApiCons;
import com.shayne.domain.Dictionary;
import com.shayne.domain.vo.ApiResult;
import com.shayne.domain.vo.PageData;
import com.shayne.domain.vo.PageImpl;
import com.shayne.service.DictionaryService;

/**
 * 字典管理
 * @Author 王小张
 * @Date 2020年8月25日 下午5:31:21
 */
@RestController
@RequestMapping(value=ApiCons.API + ApiCons.SEPARATOR + ApiCons.DICTIONARY)
public class DictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;
    
    /**
     * 新增
     * @param dictionary
     * @return ApiResult<String>
     */
    @PostMapping
    public ApiResult<String> save(@RequestBody Dictionary dictionary) {
    	ApiResult<String> result = new ApiResult<String>();
    	String msg = "SUCCESS";
    	result.setMsg(msg);
    	result.setResult(true);
    	if(null == dictionary) {
    		msg = "字典信息为空！";
    		result.setMsg(msg);
    		result.setResult(false);
    	}
    	// 写入创建时间
    	if(null == dictionary.getCreateTime()) {
    		dictionary.setCreateTime(new Date());
    	}
        
        try {
			dictionary = dictionaryService.save(dictionary);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
    		result.setResult(false);
			logger.error("保存字典发生异常：");
			e.printStackTrace();
		}
        return result;
    }
    
    /**
     * 删除
     * @param String ids
     * @return ApiResult<String>
     */
    @DeleteMapping
    public ApiResult<String> delete(@RequestParam(value="id")  String ids) {
    	ApiResult<String> result = new ApiResult<String>();
		String msg = "SUCCESS";
		try {
			String[] idsArr = ids.split(",");
			Set<Long> idsSet = new HashSet<>();
			for (String id : idsArr) {
				idsSet.add(new Long(id));
			}
			dictionaryService.delete(idsSet);
			result.setMsg(msg);
			result.setResult(true);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("删除字典发生系统异常：");
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 查询
     * @param id
     * @return ApiResult<Dictionary>
     */
    @GetMapping
    public ApiResult<Dictionary> get(@RequestParam(value="id") Long id) {
    	ApiResult<Dictionary> result = new ApiResult<Dictionary>();
		try {
			Dictionary dictionary = dictionaryService.find(id);
			result.setData(dictionary);
			result.setResult(true);
		} catch (Exception e) {
			String msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("查询字典发生异常：");
			e.printStackTrace();
		}
		return result;
    }
    
    /**
     * 查询分页数据
     * @param Integer page
     * @param Integer limit
     * @param String dicName
     * @return PageData<Dictionary>
     */
    @GetMapping(value="page")
    public PageData<Dictionary> page(
    		@RequestParam(value="page") Integer page,
            @RequestParam(value="limit") Integer limit,
            @RequestParam(value="queryKey", required=false) String dicName
            ) {
    	PageData<Dictionary> pageData = new PageData<>();
        try {
        	List<Order> orders = new ArrayList<Sort.Order>();
        	Order order1 = new Order(Direction.ASC, "typeCode");
        	Order order2 = new Order(Direction.ASC, "dicSort");
        	orders.add(order1);
        	orders.add(order2);
        	Sort sort = new Sort(orders);
        	
			PageImpl pageImpl = new PageImpl(page, limit, sort);
			if(StringUtils.isNotBlank(dicName)) {
				dicName = "%" + dicName + "%";
			}
			Page<Dictionary> p = dictionaryService.find(pageImpl, dicName);
			pageData.setCode(0);
			pageData.setCount(p.getTotalElements());
			pageData.setData(p.getContent());
			pageData.setMsg("SUCCESS");
		} catch (Exception e) {
			logger.error("查询字典列表发生系统异常：");
			e.printStackTrace();
		}
        return pageData;
    }
}

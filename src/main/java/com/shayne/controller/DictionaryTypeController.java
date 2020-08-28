package com.shayne.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shayne.constans.ApiCons;
import com.shayne.domain.DictionaryType;
import com.shayne.domain.vo.ApiResult;
import com.shayne.domain.vo.PageData;
import com.shayne.domain.vo.PageImpl;
import com.shayne.service.DictionaryTypeService;

/**
 * 字典类型管理
 * @Author 王小张
 * @Date 2020年8月25日 下午5:31:21
 */
@RestController
@RequestMapping(value=ApiCons.API + ApiCons.SEPARATOR + ApiCons.DICTYPE)
public class DictionaryTypeController extends BaseController {

    @Autowired
    private DictionaryTypeService dictionaryTypeService;
    
    /**
     * 新增
     * @param dictionaryType
     * @return ApiResult<String>
     */
    @PostMapping
    public ApiResult<String> save(@RequestBody DictionaryType dictionaryType) {
    	ApiResult<String> result = new ApiResult<String>();
    	String msg = "SUCCESS";
    	result.setMsg(msg);
    	result.setResult(true);
    	if(null == dictionaryType) {
    		msg = "字典类型信息为空！";
    		result.setMsg(msg);
    		result.setResult(false);
    	}
    	
    	// 写入创建时间
    	if(null == dictionaryType.getCreateTime()) {
    		dictionaryType.setCreateTime(new Date());
    	}
        
        try {
        	dictionaryType = dictionaryTypeService.save(dictionaryType);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
    		result.setResult(false);
			logger.error("保存字典类型发生异常：");
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
			dictionaryTypeService.delete(idsSet);
			result.setMsg(msg);
			result.setResult(true);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("删除字典类型发生系统异常：");
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 查询
     * @param id
     * @return ApiResult<DictionaryType>
     */
    @GetMapping
    public ApiResult<DictionaryType> get(@RequestParam(value="id") Long id) {
    	ApiResult<DictionaryType> result = new ApiResult<DictionaryType>();
		try {
			DictionaryType dictionaryType = dictionaryTypeService.find(id);
			result.setData(dictionaryType);
			result.setResult(true);
		} catch (Exception e) {
			String msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("查询字典类型发生异常：");
			e.printStackTrace();
		}
		return result;
    }
    
    /**
     * 查询分页数据
     * @param Integer page
     * @param Integer limit
     * @param String dicName
     * @return PageData<DictionaryType>
     */
    @GetMapping(value="page")
    public PageData<DictionaryType> page(
    		@RequestParam(value="page") Integer page,
            @RequestParam(value="limit") Integer limit,
            @RequestParam(value="queryKey", required=false) String typeName
            ) {
    	PageData<DictionaryType> pageData = new PageData<>();
        try {
        	Sort sort = new Sort(Direction.ASC, "typeSort");
			PageImpl pageImpl = new PageImpl(page, limit, sort);
			if(StringUtils.isNotBlank(typeName)) {
				typeName = "%" + typeName + "%";
			}
			Page<DictionaryType> p = dictionaryTypeService.find(pageImpl, typeName);
			pageData.setCode(0);
			pageData.setCount(p.getTotalElements());
			pageData.setData(p.getContent());
			pageData.setMsg("SUCCESS");
		} catch (Exception e) {
			logger.error("查询字典类型列表发生系统异常：");
			e.printStackTrace();
		}
        return pageData;
    }
    
    /**
     * 查询所有字典类型
     * @return ApiResult<List<DictionaryType>>
     */
    @GetMapping(value = "list")
    public ApiResult<List<DictionaryType>> list() {
    	ApiResult<List<DictionaryType>> result = new ApiResult<>();
		try {
			List<DictionaryType> list = dictionaryTypeService.findAll();
			result.setData(list);
			result.setResult(true);
		} catch (Exception e) {
			String msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("查询字典类型发生异常：");
			e.printStackTrace();
		}
		return result;
    }
}

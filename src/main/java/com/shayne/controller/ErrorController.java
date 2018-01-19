package com.shayne.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shayne.constans.ExceptionCons;

/**
 * 异常页面控制器
 * @Author WY
 * @Date 2017年12月16日
 */
@Controller
public class ErrorController extends BaseController {

    /**
     * 路径找不到
     * @return
     * ModelAndView
     */
    @GetMapping(value = ExceptionCons.NOT_FOUND)
    public ModelAndView exception404() {
        return baseExceptionModelAndView(ExceptionCons.NOT_FOUND);
    }
   
    /**
     * 拒绝连接
     * @return
     * ModelAndView
     */
    @GetMapping(value = ExceptionCons.FORBIDDEN)
    public ModelAndView exception403() {
        return baseExceptionModelAndView(ExceptionCons.FORBIDDEN);
    }
    
    /**
     * 权限不足
     * @return
     * ModelAndView
     */
    @GetMapping(value = ExceptionCons.UNAUTHORIZED)
    public ModelAndView exception401() {
        return baseExceptionModelAndView(ExceptionCons.UNAUTHORIZED);
    }
    
    /**
     * 系统错误
     * @return
     * ModelAndView
     */
    @GetMapping(value = ExceptionCons.INTERNAL_SERVER_ERROR)
    public ModelAndView exception500() {
        return baseExceptionModelAndView(ExceptionCons.INTERNAL_SERVER_ERROR);
    }
}

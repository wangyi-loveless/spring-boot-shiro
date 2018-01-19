package com.shayne.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shayne.constans.ApiCons;

/**
 * 登录控制器
 * @Author WY
 * @Date 2017年12月13日
 */
@Controller
public class MainController extends BaseController {

    /**
     * 首页
     * @return
     * ModelAndView
     */
    @GetMapping(value=ApiCons.INDEX)
    public ModelAndView index() {
        return baseUserModelAndView(ApiCons.INDEX);
    }
    
    /**
     * 菜单管理
     * @return
     * ModelAndView
     */
    @GetMapping(value=ApiCons.MENU_VIEW)
    public ModelAndView menu() {
        return baseModelAndView(ApiCons.MENU);
    }
}

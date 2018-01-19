package com.shayne.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shayne.constans.ExceptionCons;

/**
 * 异常处理类
 * @Author WY
 * @Date 2017年12月19日
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    /** 日志  */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 返回错误页面
     * @param reqest
     * @param e
     * @return
     * ModelAndView
     */
    @ExceptionHandler(value=Exception.class)
    public void defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, 
            Exception e){
        int requestType = getRequestContentType(request);
        // 判断请求类型：1-ajax，0-其他
        if(requestType == 1) {
            //TODO 此处有bug，后续完善：因为ajax请求从request里面拿不到content type，因为ajax不一定全是 json的请求类型，虽然大部分是
            String content = e.getMessage();
            write(response, content);
        }else {
            try {
                response.sendRedirect(ExceptionCons.INTERNAL_SERVER_ERROR);
            } catch (IOException IOE) {
                logger.error("跳转系统错误页面出错！", IOE);
            }
        }
    }
    
    /**
     * 判断请求类型：1-ajax，0-其他
     * @param httpRequest
     * @return
     * Integer
     */
    private int getRequestContentType(HttpServletRequest httpRequest) {
        if (httpRequest.getHeader("X-Requested-With") != null 
                && httpRequest.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            return 1;
        }
        return 0;
    }
    
    /**
     * 写错误信息
     * @param response
     * @param content
     * void
     */
    private void write(HttpServletResponse response, String content) {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(content);
        } catch (IOException e) {
            logger.error("获取PrintWriter发生异常：", e);
        }
        if(writer != null) {
            writer.close();
        }
    }
}

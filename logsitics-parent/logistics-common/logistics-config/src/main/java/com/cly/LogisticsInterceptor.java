package com.cly;

import com.cly.web.ThreadLocalAdminUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 完成登录拦截 如果有 token 设置 token
 */
public class LogisticsInterceptor extends HandlerInterceptorAdapter {

    /**
     * 这里没有直接获取用户的信息 而是采用保存 token 的方式
     * 目的是前台后台是不同的用户 使用 ThreadLocal 需要不同的进行保存
     * 直接保存 token 方式 在具体的业务当中再根据不同的要求去实现不同的用户获取信息操作
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token)) {
            ThreadLocalAdminUtils.set(token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalAdminUtils.remove();
    }

}
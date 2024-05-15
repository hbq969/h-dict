package com.github.hbq969.code.dict.config;

import com.github.hbq969.code.common.spring.interceptor.AbstractHandlerInterceptor;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/15 21:07
 */
public class PermitHandlerInterceptor extends AbstractHandlerInterceptor {

    @Autowired
    private DictConf conf;

    @Override
    public int order() {
        return 1;
    }

    @Override
    public List<String> getPathPatterns() {
        return Lists.newArrayList("/ui-dict/**");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("dict-token");
        if (!StringUtils.equals(conf.getDictToken(), token)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("Actions that are not allowed");
            return false;
        }
        return true;
    }
}

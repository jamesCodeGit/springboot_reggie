package com.blog.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.blog.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * check user login
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();

        log.info("拦截到请求: {}", requestURI);

        //1.1定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",//登录 放行
                "/employee/logout",
                "/backend/**",  //后端静态资源
                "/front/**",     //前端静态资源
        };

        //2.判断本次请求是否需要处理,如果不需要处理，则直接放行
        if (check(urls, requestURI)) {
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //3.判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee" ) != null){
            log.info("用户已登录，用户id为: {}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //4.如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
         response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        log.info("request url :{}", requestURI);
        for (String url : urls) {
            if(PATH_MATCHER.match(url, requestURI))
                return true;
        }
        return false;
    }
}

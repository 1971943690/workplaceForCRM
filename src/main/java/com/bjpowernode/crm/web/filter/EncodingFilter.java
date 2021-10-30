package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter  implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入到过滤字符编码的乱码");
        //过滤post请求的中文参数乱码
        servletRequest.setCharacterEncoding("utf-8");
        //过滤响应流响应文的乱码
        servletResponse.setContentType("text/html;charset=utf-8");
        //放行
        filterChain.doFilter(servletRequest,servletResponse);
        //部署

    }
}

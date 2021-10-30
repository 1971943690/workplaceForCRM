package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter  implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("���뵽�����ַ����������");
        //����post��������Ĳ�������
        servletRequest.setCharacterEncoding("utf-8");
        //������Ӧ����Ӧ�ĵ�����
        servletResponse.setContentType("text/html;charset=utf-8");
        //����
        filterChain.doFilter(servletRequest,servletResponse);
        //����

    }
}

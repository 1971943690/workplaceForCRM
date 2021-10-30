package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入到验证有没有登入过的过滤器");
        //向下转型  父转子
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse  = (HttpServletResponse) servletResponse;
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");

        String path = httpServletRequest.getServletPath();
        System.out.println(path);

        //不应该拦截的资源
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            //如果user存在，表示这是已经登入过的用户
            if(user != null){
                filterChain.doFilter(servletRequest,servletResponse);
            }else{

                /**
                 * 重定向到登入页面
                 * 重定向的路径怎么写
                 *      在实际项目开发中，对于路径的使用，不论操作的是前端还是后端，应该一律使用绝对路径
                 *      关于转发和重定向的路径的写法如下：
                 *          转发：
                 *                  使用的是一种特殊的绝对路径的使用方式，这样绝对路径前面不加 /项目名  ，这种路径也称之为内部路径
                 *                  /login.jsp
                 *          重定向：
                 *                  使用的是传统绝对路径的写法，前面必须以 /项目名开头，后面跟具体的资源路径
                 *
                 *       为什么使用重定向，使用转发不行吗？
                 *              转发之后，路径会停留在老路径上，而不是跳转之后最新资源的路径
                 *              我们应该为用户转到登入页面的同时，将浏览器的地址栏自动设置为当前的登入页面的路径
                 *
                 *
                 */


                //${pageContext.request.contextPath}  获得的是  -->  /项目名

                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login.jsp");

                //部署
            }
        }



    }
}

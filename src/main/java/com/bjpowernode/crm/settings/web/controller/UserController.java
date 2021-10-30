package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.impl.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path = req.getServletPath();

        if ("/settings/user/login.do".equals(path)) {
            //
            login(req, resp);
        } else if ("/settings/user/xxx.do".equals(path)) {
            //
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {

        //获得请求参数
        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");

        //先要将密码转为密文
        loginPwd = MD5Util.getMD5(loginPwd);

        //获得ip地址
        String ip = req.getRemoteAddr();
        System.out.println(ip);

        //创建service对象  未来业务层开发，统一使用代理类形态的接口对象        这个错误找了半天
        UserService userService = (UserService)ServiceFactory.getService(new UserServiceImpl());

        try{

            User user = userService.login(loginAct,loginPwd,ip);
            //将user信息添加到session对象中
            req.getSession().setAttribute("user",user);

            //如果程序进行到这里，说明业务层没有为controller抛出任何异常
            //表示登入成功  {"success":true}

            PrintJson.printJsonFlag(resp,true);

        }catch (Exception e){
            e.printStackTrace();
            // 一但程序执行到这个，表示业务层验证登入没有成功，为controller抛出异常
            //登入失败   {"success": false, }
            //得到错误信息
            String msg = e.getMessage();
            /*可以有两种手段来处理：
                （1） 将多项信息打包成map， 将map解析为json串
                （2） 创建一个 VO  （对象）
                private boolean success;
                private String msg;

                如果对于展现的信息将来还会大量的使用，我们创建一个类vo，使用方便
                如果对于展现的信息只有这个需求，就使用map
            */
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);

            PrintJson.printJsonObj(resp,map);
        }




    }
}

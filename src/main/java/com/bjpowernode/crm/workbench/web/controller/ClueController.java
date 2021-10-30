package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.impl.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends  HttpServlet{
         @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("进入到线索管理控制器");
            String path = req.getServletPath();

            if ("/workbench/clue/getUserList.do".equals(path)) {
                //
               getUserList(req,resp);

            } else if ("/workbench/clue/save.do".equals(path)) {
                save(req,resp);
        }

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {

             //从请求对象获取参数信息
            String  id =UUIDUtil.getUUID();
            String  fullname = req.getParameter("fullname");
            String  appellation = req.getParameter("appellation");
            String  owner = req.getParameter("owner");
            String  company = req.getParameter("company");
            String  job = req.getParameter("job");
            String  email = req.getParameter("email");
            String  phone = req.getParameter("phone");
            String  website = req.getParameter("website");
            String  mphone = req.getParameter("mphone");
            String  state = req.getParameter("state");
            String  source = req.getParameter("source");
            String  createBy = ((User)req.getSession().getAttribute("user")).getName();
            String  createTime = DateTimeUtil.getSysTime();
            String  description = req.getParameter("description");
            String  contactSummary = req.getParameter("contactSummary");
            String  nextContactTime = req.getParameter("nextContactTime");
            String  address = req.getParameter("address");

            Clue clue = new Clue();
            clue.setId(id);
            clue.setFullname(fullname);
            clue.setAppellation(appellation);
            clue.setOwner(owner);
            clue.setCompany(company);
            clue.setJob(job);
            clue.setEmail(email);
            clue.setPhone(phone);
            clue.setWebsite(website);
            clue.setMphone(mphone);
            clue.setSource(source);
            clue.setState(state);
            clue.setCreateBy(createBy);
            clue.setCreateTime(createTime);
            clue.setDescription(description);
            clue.setContactSummary(contactSummary);
            clue.setAddress(address);
            clue.setNextContactTime(nextContactTime);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = clueService.save(clue);
        PrintJson.printJsonFlag(resp,flag);


    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("获取用户信息表");
             UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

             List<User> uList = userService.getUserList();

             PrintJson.printJsonObj(resp,uList);

    }
}

package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.impl.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;


import javax.jws.Oneway;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动管理控制器");
        String path = req.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)) {
            //
            getUserList(req,resp);

        } else if ("/workbench/activity/save.do".equals(path)) {
            save(req,resp);

        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(req,resp);
        }else if("/workbench/activity/delete.do".equals(path)){
           delete(req,resp);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(req,resp);
        }else if("/workbench/activity/update.do".equals(path)){
            update(req,resp);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(req,resp);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(req,resp);
        }else if("/workbench/activity/deleteRemarkByAid.do".equals(path)){
           deleteRemarkByAid(req,resp);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(req,resp);
        }else if("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(req,resp);
        }

    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {

        String id = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String editFlag = "1";

        ActivityRemark activity = new ActivityRemark();
        activity.setId(id);
        activity.setNoteContent(noteContent);
        activity.setEditFlag(editFlag);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.updateRemark(activity);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",activity);

        PrintJson.printJsonObj(resp,map);


    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {
        String id = UUIDUtil.getUUID();
        String activityId = req.getParameter("activityId");
        String noteContent = req.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setCreateBy(createBy);
        activityRemark.setId(id);
        activityRemark.setActivityId(activityId);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setNoteContent(noteContent);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.saveRemark(activityRemark);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",activityRemark);

        PrintJson.printJsonObj(resp,map);

    }

    private void deleteRemarkByAid(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.deleteRemarkByAid(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp) {
        //
        String activityId = req.getParameter("activityId");
        ActivityService  activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> activityRemarkList =  activityService.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(resp,activityRemarkList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //传统的请求转发

        String id = req.getParameter("id");
        ActivityService  activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity =  activityService.detail(id);
        //保存请求转发域
        req.setAttribute("a",activity);
        //请求转发
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }

    private void update(HttpServletRequest request, HttpServletResponse resp) {
        System.out.println("执行市场活动修改操作");

        String  id = request.getParameter("id");
        String  owner = request.getParameter("owner");
        String  name = request.getParameter("name");
        String  startDate = request.getParameter("startDate");
        String  endDate = request.getParameter("endDate");
        String  cost = request.getParameter("cost");
        String  description = request.getParameter("description");

        //创建当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登入用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(createTime);
        activity.setEditBy(createBy);


        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.update(activity);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");

        ActivityService   activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = req.getParameter("id");
        //总结：
                //controller调用service方法，返回值应该是什么
                //你得想一想前端要什么，就要从service层取什么
        //前端要什么，管业务层去要
        /*
        uList
        a

        * */
        Map<String, Object> map =   activityService.getUserListAndActivity(id);
        PrintJson.printJsonObj(resp,map);




    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场的删除操作");
        String ids[] = req.getParameterValues("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.delete(ids);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询+分页查询）");

        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        String pageNoStr = req.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //计算每页展现的记录数
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String, Object>  maps = new HashMap<String, Object>();
        maps.put("name",name);
        maps.put("owner",owner);
        maps.put("startDate",startDate);
        maps.put("endDate",endDate);
        maps.put("pageSize",pageSize);
        maps.put("skipCount",skipCount);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        PaginationVO<Activity> vo =  activityService.pageList(maps);
        PrintJson.printJsonObj(resp,vo);

         /*
         前端要：市场的活动信息泪飙
                查询的总条数

                业务层拿到了以上信息之后，如果做返回
                map
                map.put("dataList",dataList);
                map.put("total",total);
                PrintJson map ----> json
                {"total":100,"dataList":[{市场活动1},{市场活动2}]}

                vo
                PaginationVO<T>
                    private int total;
                    private List<T> dataList;
                  将来分页查询，每个模块都有，所以我们选择使用一个通过vo，操作起来比较方便
         * */

    }

    public void getUserList(HttpServletRequest req, HttpServletResponse resp){

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(resp,userList);
    }
    public void save(HttpServletRequest request, HttpServletResponse resp){

        System.out.println("执行市场活动添加操作");

      String  id = UUIDUtil.getUUID();
      String  owner = request.getParameter("owner");
      String  name = request.getParameter("name");
      String  startDate = request.getParameter("startDate");
      String  endDate = request.getParameter("endDate");
      String  cost = request.getParameter("cost");
      String  description = request.getParameter("description");

      //创建当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登入用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);


        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.save(activity);

        PrintJson.printJsonFlag(resp,flag);

    }

}

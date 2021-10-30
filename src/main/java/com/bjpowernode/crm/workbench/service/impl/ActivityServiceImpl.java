package com.bjpowernode.crm.workbench.service.impl;


import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);

        if(count != 1){
            flag = false;
        }
        return flag;
    }



    public PaginationVO<Activity> pageList(Map<String, Object> maps) {

        //取得 total
        int total = activityDao.getTotalCountByCondition(maps);

        //取得 dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(maps);


        //创建一个vo对象，把  total 和  dataList封装起来
        PaginationVO<Activity> vo  = new PaginationVO<Activity>();
        vo.setDataList(dataList);
        vo.setTotal(total);
        return vo;
    }

    public boolean delete(String[] ids) {


        boolean flag = true;
        //查询要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回影响的条数，实际删除的条数
        int count2 = activityRemarkDao.deleteByAids(ids);

        if(count1 != count2){
            flag = false;
        }

        int count3 = activityDao.delete(ids);
        if(count3 != ids.length){
            flag = false;
        }

        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> userList = userDao.getUserList();

        //取a
        Activity a = activityDao.getById(id);

        //将uList和a打包成map 返回
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",userList);     //根据前端要获得的名称  所以  key 要为 uList
        map.put("a",a);
        return map;

    }

    public boolean update(Activity activity) {
        boolean flag = true;

        int count = activityDao.update(activity);

        if(count != 1){
            flag = false;
        }
        return flag;
    }

    public Activity detail(String id) {

        Activity a = activityDao.detail(id);
        return a;
    }

    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> aList = activityRemarkDao.getRemarkListByAid(activityId);

        return aList;
    }

    public boolean deleteRemarkByAid(String id) {
        boolean flag = true;
        int count  = activityRemarkDao.deleteRemarkByAid(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    public boolean saveRemark(ActivityRemark activityRemark) {

        boolean flag = true;
       int count = activityRemarkDao.saveRemark(activityRemark);
       if(count != 1){
           flag = false;
       }
       return flag;
    }

    public boolean updateRemark(ActivityRemark activity) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(activity);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}

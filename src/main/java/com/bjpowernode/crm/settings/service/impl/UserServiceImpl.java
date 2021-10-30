package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.LoginException;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService{
     //获得代理对象
     private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

     public User login(String loginAct, String loginPwd, String ip) throws LoginException {

          Map<String,String> map = new HashMap<String, String>();
          map.put("loginAct",loginAct);
          map.put("loginPwd",loginPwd);
          User user = userDao.login(map);


          //如果没有user为空，则未查询到该账号和密码
          if(user == null){
               throw new LoginException("账号密码错误");
          }

          //继续验证接下来三项
          String expireTime = user.getExpireTime();
          String currTime = DateTimeUtil.getSysTime();
          if(currTime.compareTo(expireTime) > 0){
               throw new LoginException("账号已过期");
          }

          String allowIps = user.getAllowIps();
          if(!allowIps.contains(ip)){
               throw new LoginException("ip非法访问");
          }

          String lockState = user.getLockState();
          if("0".equals(lockState))
          {
               throw new LoginException("账号已锁定");
          }
          return user;
     }
     public List<User> getUserList(){
          List<User> userList = userDao.getUserList();
          return userList;
     }
}

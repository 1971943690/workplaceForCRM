package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.impl.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.Unmarshaller;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    /*
    该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
    对象创建完毕后，马上执行该方法

    sce ：该参数能够取得监听的对象
            监听的是什么对象，就可一通过该参数能取得什么对象
            例如我们现在监听的是上下文域对象，通过该参数就可以取得上下文域对象
    * */

    public void contextInitialized(ServletContextEvent sce) {
       // System.out.println("上下文域对象创建了");
        //System.out.println(123);

        //获得上下文域对象
        ServletContext application = sce.getServletContext();

        //需要业务层 service   返回一个 map

        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = dicService.getAll();

        //把拿到的map 解析为 保存到上下文域对象的键值对
        Set<String> stringSet = map.keySet();
        for(String str : stringSet){
            application.setAttribute(str,map.get(str));
        }
    }

}

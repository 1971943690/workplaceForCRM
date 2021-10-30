package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao cluDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);


    public boolean save(Clue clue) {
        //
        boolean flag = true;

        int count = cluDao.save(clue);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}

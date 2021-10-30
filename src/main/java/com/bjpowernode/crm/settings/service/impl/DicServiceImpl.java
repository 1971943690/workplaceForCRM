package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService{
    private DicTypeDao  dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    public Map<String, List<DicValue>> getAll() {
        //将字典中的七种类型取出
        List<DicType> typeList =dicTypeDao.getTypeList();
        //遍历列表， 通过类型来取得对应的值
        Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();
        for(DicType type: typeList){
            //获取其中的一种类型
            String code = type.getCode();
            // 通过这个类型来获得类型对应的 list 值
            List<DicValue> valueList = dicValueDao.getValueList(code);
           map.put(code,valueList);
        }
        return map;
    }
}

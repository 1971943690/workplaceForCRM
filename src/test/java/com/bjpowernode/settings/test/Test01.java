package com.bjpowernode.settings.test;

import com.bjpowernode.crm.utils.MD5Util;

public class Test01 {
    public static void main(String[] args) {
        System.out.println(1234);
        String str = "wh385024WH";
        String pwd = MD5Util.getMD5(str);
        System.out.println(pwd);
        System.out.println(111);
        System.out.println(222);

    }
}

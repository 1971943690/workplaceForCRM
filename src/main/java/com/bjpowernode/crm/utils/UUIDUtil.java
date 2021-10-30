package com.bjpowernode.crm.utils;

import java.util.UUID;

public class UUIDUtil {

	//生成世界独一无二的字符串
	public static String getUUID(){
		
		return UUID.randomUUID().toString().replaceAll("-","");
		
	}
	
}

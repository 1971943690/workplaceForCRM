package com.bjpowernode.crm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	
	public static String getSysTime(){
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		Date date = new Date();
//		String dateStr = sdf.format(date);
//

		//创建一个日期格式对象     将日期格式化后返回
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = new Date();
		String dateStr1 = simpleDateFormat.format(date1);
		return dateStr1;
	}
	
}

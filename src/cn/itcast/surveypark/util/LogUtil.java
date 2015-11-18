package cn.itcast.surveypark.util;

import java.util.Calendar;

public class LogUtil {
	
	/**
	 * 动态生成表名
	 */
	public static String generateLogTableName(int offset){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1 + offset;//1-12
		
		if(month > 12){
			year ++ ;
			month = month - 12 ;
		}
		else if(month < 1){
			year -- ;
			month = month + 12 ;
		}
		return "logs_" + year + "_" + month ;
	}
}

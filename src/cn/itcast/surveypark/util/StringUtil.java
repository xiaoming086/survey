package cn.itcast.surveypark.util;

/**
 * StringUtil 
 */
public class StringUtil {
	/**
	 * 将字符串拆分成数组
	 */
	public static String[] str2Arr(String str,String tag){
		if(ValidateUtil.isValid(str)){
			return str.split(tag);
		}
		return null ;
	}

	/**
	 * 判断数组中是否含有指定的串
	 */
	public static boolean contains(String[] arr, String value) {
		if(ValidateUtil.isValid(arr)){
			for(String s : arr){
				if(s.equals(value)){
					return true ;
				}
			}
		}
		return false;
	}

	/**
	 * 将数组转换成字符串,使用","分隔
	 */
	public static String arr2Str(Object[] value) {
		String temp = "" ;
		if(ValidateUtil.isValid(value)){
			for(Object o : value){
				temp = temp + o + ",";
			}
			return temp.substring(0, temp.length() -1 );
		}
		return null;
	}
	
	public static String getDescString(String str){
		if(ValidateUtil.isValid(str) && str.length() > 30){
			return str.substring(0, 29);
		}
		return str ;
	}
}

package cn.itcast.surveypark.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 * 自定义缓存可以生成器
 */
public class SurveyKeyGenerator implements KeyGenerator {

	public Object generate(Object arg0, Method arg1, Object... arg2) {
		String targHashCode = arg0.getClass().getSimpleName() + "["+arg0.hashCode()+"]" ;
		String mname = arg1.getName();
		String key = null ;
		if(ValidateUtil.isValid(arg2)){
			StringBuffer buffer = new StringBuffer();
			for(Object p : arg2){
				buffer.append(p.toString() + ",");
			}
			key = targHashCode + "." + mname + "("+DataUtil.md5(buffer.toString())+")"; 
			System.out.println(key);
			return key ;
		}
		key = targHashCode + "." + mname +"()";
		System.out.println(key);
		return key;
	}
}

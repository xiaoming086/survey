package cn.itcast.surveypark.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.struts2.action.BaseAction;
import cn.itcast.surveypark.struts2.action.UserAware;

/**
 * ValidateUtil
 */
public class ValidateUtil {
	/**
	 * 判断字符串有效性 
	 */
	public static boolean isValid(String str){
		if(str == null || "".equals(str.trim())){
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断集合的有效性
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Collection col){
		if(col == null || col.isEmpty()){
			return false ;
		}
		return true ;
	}

	/**
	 * 判断数组是否有效 
	 */
	public static boolean isValid(Object[] arr) {
		if(arr == null || arr.length == 0){
			return false;
		}
		return true ;
	}
	
	/**
	 * 判断是否有权限
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasRight(String ns,String actionName,HttpServletRequest req,BaseAction action){
		if(!ValidateUtil.isValid(ns)
				|| "/".equals(ns)){
			ns = "" ;
		}
		
		//处理?参数
		if(actionName.contains("?")){
			actionName = actionName.substring(0, actionName.indexOf("?"));
		}
		
		String url = ns + "/" + actionName ;
		HttpSession s = req.getSession();
		ServletContext sc = s.getServletContext();
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		Right r = map.get(url);
		if(r == null){
			return false ; 
		}
		//公共资源
		if(r.isCommon()){
			return true ;
		}
		else{
			User user = (User) s.getAttribute("user");
			//登录?
			if(user == null){
				return false ;
			}
			else{
				//userAware
				if(action != null 
						&& action instanceof UserAware){
					((UserAware)action).setUser(user);
				}
				//超级管理员?
				if(user.isSuperAdmin()){
					return true ;
				}
				else{
					//有权限?
					if(user.hasRight(r)){
						return true ;
					}
					else{
						return false ;
					}
				}
			}
			
		}
	}
}

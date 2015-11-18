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
	 * �ж��ַ�����Ч�� 
	 */
	public static boolean isValid(String str){
		if(str == null || "".equals(str.trim())){
			return false ;
		}
		return true ;
	}
	
	/**
	 * �жϼ��ϵ���Ч��
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Collection col){
		if(col == null || col.isEmpty()){
			return false ;
		}
		return true ;
	}

	/**
	 * �ж������Ƿ���Ч 
	 */
	public static boolean isValid(Object[] arr) {
		if(arr == null || arr.length == 0){
			return false;
		}
		return true ;
	}
	
	/**
	 * �ж��Ƿ���Ȩ��
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasRight(String ns,String actionName,HttpServletRequest req,BaseAction action){
		if(!ValidateUtil.isValid(ns)
				|| "/".equals(ns)){
			ns = "" ;
		}
		
		//����?����
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
		//������Դ
		if(r.isCommon()){
			return true ;
		}
		else{
			User user = (User) s.getAttribute("user");
			//��¼?
			if(user == null){
				return false ;
			}
			else{
				//userAware
				if(action != null 
						&& action instanceof UserAware){
					((UserAware)action).setUser(user);
				}
				//��������Ա?
				if(user.isSuperAdmin()){
					return true ;
				}
				else{
					//��Ȩ��?
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

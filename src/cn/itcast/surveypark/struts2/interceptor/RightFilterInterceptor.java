package cn.itcast.surveypark.struts2.interceptor;

import org.apache.struts2.ServletActionContext;

import cn.itcast.surveypark.struts2.action.BaseAction;
import cn.itcast.surveypark.util.ValidateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 权限表过滤拦截器
 */
public class RightFilterInterceptor implements Interceptor {

	private static final long serialVersionUID = -5671658363347813780L;

	public void destroy() {
	}

	public void init() {
	}

	@SuppressWarnings("rawtypes")
	public String intercept(ActionInvocation invocation) throws Exception {
		BaseAction action = (BaseAction) invocation.getAction() ;
		
		ActionProxy proxy = invocation.getProxy();
		String ns = proxy.getNamespace();
		String actionName = proxy.getActionName();
		if(ValidateUtil.hasRight(ns, actionName, ServletActionContext.getRequest(), action)){
			return invocation.invoke();
		}
		else{
			return "no_right_error" ;
		}
	}
}

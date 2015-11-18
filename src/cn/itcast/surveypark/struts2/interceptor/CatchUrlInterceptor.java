package cn.itcast.surveypark.struts2.interceptor;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.util.ValidateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * ²¶»ñurlÀ¹½ØÆ÷
 */
public class CatchUrlInterceptor implements Interceptor {

	private static final long serialVersionUID = 3306117036174630812L;

	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		String ns = proxy.getNamespace() ; 
		String actionName = proxy.getActionName();
		if(!ValidateUtil.isValid(ns)
				|| ns.equals("/")){
			ns = "" ;
		}
		String url = ns + "/" + actionName ;
		
		ServletContext sc = ServletActionContext.getServletContext();
//		ApplicationContext ac = (ApplicationContext) sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
//		RightService rs = (RightService) ac.getBean("rightService");
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		RightService rs = (RightService) ac.getBean("rightService");
		rs.appendRightByURL(url);
		return invocation.invoke();
	}
}

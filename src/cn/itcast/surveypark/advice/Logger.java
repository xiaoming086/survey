package cn.itcast.surveypark.advice;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.surveypark.domain.Log;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.LogService;
import cn.itcast.surveypark.util.StringUtil;

/**
 * 日志记录仪
 */
public class Logger {
	private LogService logService ;
	
	//注入logService
	public void setLogService(LogService logService) {
		this.logService = logService;
	}


	/**
	 * 记录日志 
	 */
	public Object record(ProceedingJoinPoint pjp){
		Log log = new Log();
		try {
			ActionContext ac = ActionContext.getContext();
			//operator
			if(ac != null){
				HttpServletRequest req = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
				if(req != null){
					User user = (User) req.getSession().getAttribute("user");
					if(user != null){
						log.setOperator("" + user.getId() + ":" + user.getEmail());
					}
				}
			}
			
			//operName,方法名
			String methodName = pjp.getSignature().getName();
			log.setOperName(methodName);
			
			//operParams,方法参数列表
			Object[] args = pjp.getArgs();
			log.setOperParams(StringUtil.arr2Str(args));
			
			//调用目标对象的方法
			Object ret = pjp.proceed();
			
			//operResult,成功
			log.setOperResult("success");
			
			//resultMsg,结果消息
			if(ret != null){
				log.setResultMsg(ret.toString());
			}
			return ret ;
		} catch (Throwable e) {
			log.setOperResult("failure") ;
			log.setResultMsg(e.getMessage());
		}
		finally{
			logService.saveEntity(log);
		}
		return null ;
	}
}

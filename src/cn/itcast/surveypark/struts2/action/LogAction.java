package cn.itcast.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Log;
import cn.itcast.surveypark.service.LogService;

/**
 * LogAction
 */
@Controller
@Scope("prototype")
public class LogAction extends BaseAction<Log> {

	private static final long serialVersionUID = -3808519856289029355L;

	private List<Log> allLogs ;
	
	@Resource
	private LogService logService ;
	
	public List<Log> getAllLogs() {
		return allLogs;
	}

	public void setAllLogs(List<Log> allLogs) {
		this.allLogs = allLogs;
	}

	/**
	 * 查看所有日志
	 */
	public String findAllLogs(){
		this.allLogs = logService.findNearestLogs();
		return "logListPage" ;
	}
}

package cn.itcast.surveypark.service;

import java.util.List;

import cn.itcast.surveypark.domain.Log;

public interface LogService extends BaseService<Log> {

	/**
	 * 查询最近的日志信息,默认是2个月
	 */
	public List<Log> findNearestLogs();
}

package cn.itcast.surveypark.service;

import java.util.List;

import cn.itcast.surveypark.domain.Log;

public interface LogService extends BaseService<Log> {

	/**
	 * ��ѯ�������־��Ϣ,Ĭ����2����
	 */
	public List<Log> findNearestLogs();
}

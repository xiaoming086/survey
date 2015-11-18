package cn.itcast.surveypark.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.id.UUIDHexGenerator;
import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.Log;
import cn.itcast.surveypark.service.LogService;
import cn.itcast.surveypark.util.LogUtil;

/**
 * logService
 */
@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {
	UUIDHexGenerator id = new UUIDHexGenerator();
	/**
	 * ��д�÷���,����ע��
	 */
	@Resource(name = "logDao")
	public void setDao(BaseDao<Log> dao) {
		super.setDao(dao);
	}
	
	public void saveEntity(Log t) {
		String sql = "insert into " + LogUtil.generateLogTableName(0)
				+ "(id,operator,opertime,opername,operparams,operresult,resultmsg) values(?,?,?,?,?,?,?)";
		String uuid = (String) id.generate(null, null);
		this.executeSQL(sql, 
				uuid,
				t.getOperator(),
				t.getOperTime(),
				t.getOperName(),
				t.getOperParams(),
				t.getOperResult(),
				t.getResultMsg());
	}
	
	/**
	 * ��ѯ�������־��Ϣ,Ĭ����2����
	 */
	public List<Log> findNearestLogs(){
		String sql = "select * from " + LogUtil.generateLogTableName(-1) 
					+ " union "
					+ " select * from " + LogUtil.generateLogTableName(0);
		return this.findObjectBySQL(sql) ;
	}
}

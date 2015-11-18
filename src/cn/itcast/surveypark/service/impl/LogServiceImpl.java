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
	 * 重写该方法,覆盖注解
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
	 * 查询最近的日志信息,默认是2个月
	 */
	public List<Log> findNearestLogs(){
		String sql = "select * from " + LogUtil.generateLogTableName(-1) 
					+ " union "
					+ " select * from " + LogUtil.generateLogTableName(0);
		return this.findObjectBySQL(sql) ;
	}
}

package cn.itcast.surveypark.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.itcast.surveypark.service.LogService;
import cn.itcast.surveypark.util.LogUtil;

/**
 * ��ʼ����־�������
 */
@SuppressWarnings("rawtypes")
@Component
public class IniLogTablesListener implements ApplicationListener{

	@Resource
	private LogService logService;
	
	public void onApplicationEvent(ApplicationEvent arg0) {
		//�Ƿ���������ˢ���¼�
		if(arg0 instanceof ContextRefreshedEvent){
			String tableName = LogUtil.generateLogTableName(0);
			String sql = "create table if not exists " + tableName + " like logs";
			logService.executeSQL(sql);
			
			sql = "create table if not exists " + LogUtil.generateLogTableName(1) + " like logs";
			logService.executeSQL(sql);
			
			sql = "create table if not exists " + LogUtil.generateLogTableName(2) + " like logs";
			logService.executeSQL(sql);
			System.out.println("��־��-"+tableName+",��ʼ�����");
		}
	}
}

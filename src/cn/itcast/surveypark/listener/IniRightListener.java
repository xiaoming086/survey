package cn.itcast.surveypark.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.service.RightService;

/**
 * ��ʼ��Ȩ�޼�����
 */
@SuppressWarnings("rawtypes")
@Component
public class IniRightListener implements ApplicationListener,ServletContextAware{

	@Resource
	private RightService rs ;
	
	//����servletContext����
	private ServletContext sc;
	
	public void onApplicationEvent(ApplicationEvent arg0) {
		//�Ƿ���������ˢ���¼�
		if(arg0 instanceof ContextRefreshedEvent){
			List<Right> list = rs.findAllEntities();
			Map<String, Right> map = new HashMap<String, Right>();
			for(Right r : list){
				map.put(r.getRightUrl(), r);
			}
			if(sc != null){
				sc.setAttribute("all_rights_map", map);
				System.out.println("Ȩ�޳�ʼ�������!!");
			}
		}
	}

	//ע��sc
	public void setServletContext(ServletContext servletContext) {
		//
		System.out.println("ע��sc");
		this.sc = servletContext ;
	}
}

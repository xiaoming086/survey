package cn.itcast.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;
import cn.itcast.surveypark.service.UserService;

/**
 * TestSurveyService
 */
public class TestSurveyService {
	private static ApplicationContext ac = null ;
	
	@BeforeClass
	public static void iniAC(){
		ac = new ClassPathXmlApplicationContext("beans.xml");
	}
	
	@Test
	public void newSurvey(){
		SurveyService ss = (SurveyService) ac.getBean("surveyService");
		User u = new User();
		u.setId(4);
		ss.newSurvey(u);
	}
	@Test
	public void getSurvey(){
		SurveyService ss = (SurveyService) ac.getBean("surveyService");
		ss.getSurvey(1);
	}
}

package cn.itcast.surveypark.datasource;

import cn.itcast.surveypark.domain.Survey;

/**
 * ��������,�󶨵���ǰ���߳�,����������Դ·����.���зֿ��ж�
 */
public class SurveyToken {
	
	private Survey currentSurvey ;
	
	private static ThreadLocal<SurveyToken> t = new ThreadLocal<SurveyToken>();
	
	public Survey getCurrentSurvey() {
		return currentSurvey;
	}

	public void setCurrentSurvey(Survey currentSurvey) {
		this.currentSurvey = currentSurvey;
	}

	/**
	 * �����ƶ���󶨵���ǰ�߳�
	 */
	public static void bindingToken(SurveyToken token){
		t.set(token);
	}
	
	/**
	 * �ӵ�ǰ�߳�ȡ�ð󶨵����ƶ���
	 */
	public static SurveyToken getCurrentToken(){
		return t.get() ;
	}
	
	/**
	 * ������Ƶİ�
	 */
	public static void unbindToken(){
		t.remove() ;
	}
}

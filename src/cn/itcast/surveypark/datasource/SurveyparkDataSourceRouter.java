package cn.itcast.surveypark.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * �Զ�������Դ·����
 */
public class SurveyparkDataSourceRouter extends AbstractRoutingDataSource {

	protected Object determineCurrentLookupKey() {
		SurveyToken token = SurveyToken.getCurrentToken();
		if(token != null){
			int id = token.getCurrentSurvey().getId();
			//�����
			SurveyToken.unbindToken();
			return (id % 2) == 0?"even":"odd" ;
		}
		return null;
	}
}

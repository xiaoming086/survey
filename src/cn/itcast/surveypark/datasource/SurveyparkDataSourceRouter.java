package cn.itcast.surveypark.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义数据源路由器
 */
public class SurveyparkDataSourceRouter extends AbstractRoutingDataSource {

	protected Object determineCurrentLookupKey() {
		SurveyToken token = SurveyToken.getCurrentToken();
		if(token != null){
			int id = token.getCurrentSurvey().getId();
			//解除绑定
			SurveyToken.unbindToken();
			return (id % 2) == 0?"even":"odd" ;
		}
		return null;
	}
}

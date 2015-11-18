package cn.itcast.surveypark.service;

import cn.itcast.surveypark.domain.statistics.QuestionStatisticsModel;

public interface StatisticsService {
	public QuestionStatisticsModel statistics(Integer qid);
}

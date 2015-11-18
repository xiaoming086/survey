package cn.itcast.surveypark.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.statistics.OptionStatisticsModel;
import cn.itcast.surveypark.domain.statistics.QuestionStatisticsModel;
import cn.itcast.surveypark.service.StatisticsService;

/**
 * 统计服务
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

	@Resource(name="questionDao")
	private BaseDao<Question> questionDao ;
	
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao ;
	
	/**
	 * 统计问题,返回问题统计模型
	 */
	public QuestionStatisticsModel statistics(Integer qid) {
		QuestionStatisticsModel qsm = new QuestionStatisticsModel();
		Question q = questionDao.getEntity(qid);
		qsm.setQuestion(q);
		
		//统计问题回答人数
		String hql = "select count(*) from Answer a where a.questionId = ?" ;
		int qcount = ((Long)answerDao.uniqueResult(hql,qid)).intValue();
		qsm.setCount(qcount);
		
		//选项统计hql
		String ohql = "select count(*) from Answer a where a.questionId = ? and concat(',',a.answerIds,',') like ?" ;
		//选项人数
		int ocount = 0;
		
		//统计选项
		int qt = q.getQuestionType();
		switch(qt){
			//非矩阵问题
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				String[] optArr = q.getOptionArr();
				OptionStatisticsModel osm = null ;
				//统计每个选项
				for(int i = 0 ; i < optArr.length ; i ++){
					osm = new OptionStatisticsModel();
					osm.setOptionLabel(optArr[i]);
					osm.setOptionIndex(i);
					ocount = ((Long)answerDao.uniqueResult(ohql, qid,"%,"+i+",%")).intValue();
					osm.setCount(ocount);
					qsm.getOsms().add(osm);
				}
				//其他项统计
				if(q.isOther()){
					osm = new OptionStatisticsModel();
					osm.setOptionLabel("其他");
					ocount = ((Long)answerDao.uniqueResult(ohql, qid,"%other%")).intValue();
					osm.setCount(ocount);
					qsm.getOsms().add(osm);
				}
				break ;
				
			//矩阵问题
			case 6:
			case 7:
			case 8:
				String[] rows = q.getMatrixRowTitleArr();
				String[] cols = q.getMatrixColTitleArr();
				String[] opts = q.getMatrixSelectOptionArr();
				for(int i = 0 ; i < rows.length ; i ++){
					for(int j = 0 ; j < cols.length ; j ++){
						//radio|checkbox
						if(qt != 8){
							osm = new OptionStatisticsModel();
							osm.setMatrixRowLabel(rows[i]);
							osm.setMatrixRowIndex(i);
							osm.setMatrixColLabel(cols[j]);
							osm.setMatrixColIndex(j);
							ocount = ((Long)answerDao.uniqueResult(ohql, qid,"%," + i + "_" + j + ",%")).intValue();
							osm.setCount(ocount);
							qsm.getOsms().add(osm);
						}
						//select
						else{
							for(int k = 0 ; k < opts.length ; k ++){
								osm = new OptionStatisticsModel();
								osm.setMatrixRowLabel(rows[i]);
								osm.setMatrixRowIndex(i);
								osm.setMatrixColLabel(cols[j]);
								osm.setMatrixColIndex(j);
								osm.setMatrixSelectLabel(opts[k]);
								osm.setMatrixSelectIndex(k);
								ocount = ((Long)answerDao.uniqueResult(ohql, qid,"%," + i + "_" + j + "_" + k + ",%")).intValue();
								osm.setCount(ocount);
								qsm.getOsms().add(osm);
							}
						}
					}
				}
				break ;
		}
		return qsm;
	}

}

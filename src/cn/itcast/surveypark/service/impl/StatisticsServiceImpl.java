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
 * ͳ�Ʒ���
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

	@Resource(name="questionDao")
	private BaseDao<Question> questionDao ;
	
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao ;
	
	/**
	 * ͳ������,��������ͳ��ģ��
	 */
	public QuestionStatisticsModel statistics(Integer qid) {
		QuestionStatisticsModel qsm = new QuestionStatisticsModel();
		Question q = questionDao.getEntity(qid);
		qsm.setQuestion(q);
		
		//ͳ������ش�����
		String hql = "select count(*) from Answer a where a.questionId = ?" ;
		int qcount = ((Long)answerDao.uniqueResult(hql,qid)).intValue();
		qsm.setCount(qcount);
		
		//ѡ��ͳ��hql
		String ohql = "select count(*) from Answer a where a.questionId = ? and concat(',',a.answerIds,',') like ?" ;
		//ѡ������
		int ocount = 0;
		
		//ͳ��ѡ��
		int qt = q.getQuestionType();
		switch(qt){
			//�Ǿ�������
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				String[] optArr = q.getOptionArr();
				OptionStatisticsModel osm = null ;
				//ͳ��ÿ��ѡ��
				for(int i = 0 ; i < optArr.length ; i ++){
					osm = new OptionStatisticsModel();
					osm.setOptionLabel(optArr[i]);
					osm.setOptionIndex(i);
					ocount = ((Long)answerDao.uniqueResult(ohql, qid,"%,"+i+",%")).intValue();
					osm.setCount(ocount);
					qsm.getOsms().add(osm);
				}
				//������ͳ��
				if(q.isOther()){
					osm = new OptionStatisticsModel();
					osm.setOptionLabel("����");
					ocount = ((Long)answerDao.uniqueResult(ohql, qid,"%other%")).intValue();
					osm.setCount(ocount);
					qsm.getOsms().add(osm);
				}
				break ;
				
			//��������
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

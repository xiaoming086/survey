package cn.itcast.surveypark.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 * SurveyServiceImpl
 */
@Service("surveyService")
public class SurveyServiceImpl implements SurveyService {

	//ע��surveyDao
	@Resource(name="surveyDao")
	private BaseDao<Survey> surveyDao ;
	
	//ע��pageDao
	@Resource(name="pageDao")
	private BaseDao<Page> pageDao ;
	
	
	//ע��questionDao
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao ;
	
	//ע��answerDao
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao ;
	
	/**
	 * �½����� 
	 */
	public Survey newSurvey(User u){
		Survey s = new Survey();
		Page p = new Page();
		
		//���õ����user�Ĺ�ϵ
		s.setUser(u);
		p.setSurvey(s);
		s.getPages().add(p);
		
		//����
		surveyDao.saveEntity(s);
		pageDao.saveEntity(p);
		return s ;
	}
	
	/**
	 * ��ѯָ���û����鼯��
	 */
	public List<Survey> findMySurveys(User u){
		String hql = "from Survey s where s.user.id = ?" ;
		return surveyDao.findEntityByHQL(hql,u.getId());
	}
	

	/**
	 * ����id��ѯ����
	 */
	public Survey getSurvey(Integer sid){
		return surveyDao.getEntity(sid); 
	}
	
	/**
	 * ����id��ѯ����,Я�����к���
	 */
	public Survey getSurveyWithChildren(Integer sid){
		Survey s = this.getSurvey(sid);
		//��ʼ�����⼯�Ϻ�ҳ�漯��
		for(Page p : s.getPages()){
			p.getQuestions().size();
		}
		return s; 
	}
	
	/**
	 * ���µ���
	 */
	public void updateSurvey(Survey model){
		surveyDao.updateEntity(model);
	}
	

	/**
	 *  ����/����ҳ��
	 */
	public void saveOrUpdatePage(Page model){
		pageDao.saveOrUpdateEntity(model);
	}
	
	/**	
	 * �༭ҳ�����
	 */
	public Page getPage(Integer pid){
		return pageDao.getEntity(pid);
	}
	
	/**
	 * ����/��������
	 */
	public void saveOrUpdateQuestion(Question model){
		questionDao.saveOrUpdateEntity(model);
	}
	
	/**
	 * �༭����
	 */
	public Question getQuestion(Integer qid){
		return questionDao.getEntity(qid);
	}
	
	/**
	 * ɾ������
	 */
	public void deleteQuestion(Integer qid){
		//answers
		String hql = "delete from Answer a where a.questionId = ?" ;
		answerDao.batchEntityByHQL(hql,qid);
		//question
		hql = "delete from Question q where q.id = ?" ;
		questionDao.batchEntityByHQL(hql,qid);
	}
	
	/**
	 * ɾ��ҳ��
	 */
	public void deletePage(Integer pid){
		//answers
		String hql = "delete from Answer a where a.questionId in (select q.id from Question q where q.page.id = ?)" ;
		answerDao.batchEntityByHQL(hql,pid);
		//questions
		hql = "delete from Question q where q.page.id = ?" ;
		questionDao.batchEntityByHQL(hql,pid);
		//page
		hql = "delete from Page p where p.id = ?" ;
		pageDao.batchEntityByHQL(hql,pid);
	}
	
	/**
	 * ɾ������ 
	 */
	public void deleteSurvey(Integer sid){
		//1.answers
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);

		//2.questions
		//hibernate��֧���������ϵ�д����.����ʹ�� delete xxx .. from Question q where q.page.survey.id = ?
		hql = "delete from Question q where q.page.id in (select p.id from Page p where p.survey.id = ?)" ;
		questionDao.batchEntityByHQL(hql,sid);

		//3.pages
		hql = "delete from Page p where p.survey.id = ?" ;
		pageDao.batchEntityByHQL(hql,sid);

		//4.survey
		hql = "delete from Survey s where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql,sid);
	}
	
	/**
	 * �����
	 */
	public void clearAnswers(Integer sid){
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);
	}
	
	/**
	 * �л�״̬ 
	 */
	public void toggleStatus(Integer sid){
		Survey s = this.getSurvey(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql,!s.isClosed(),sid);
	}
	
	/**
	 * �������ݿ�·����Ϣ
	 */
	public void updateLogoPhotoPath(Integer sid, String path){
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql, path,sid);
	}
	
	/**
	 * ��ѯ����,Я��page����
	 */
	public List<Survey> findSurveysWithPage(User user){
		String hql = "from Survey s where s.user.id = ?"; 
		List<Survey> list = surveyDao.findEntityByHQL(hql,user.getId());
		//ǿ�г�ʼ��ҳ�漯��
		for(Survey s: list){
			s.getPages().size();
		}
		return list ;
	}
	
	/**
	 * �����ƶ��͸��Ʋ���
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos){
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		//�ƶ�����?
		if(srcSurvey.getId().equals(targSurvey.getId())){
			setOrderno(srcPage,targPage,pos);
		}
		//����
		else{
			//��Դҳ�������ȸ���
			srcPage.getQuestions().size();
			Page copy = (Page) DataUtil.deeplyCopy(srcPage) ; //��srcPage������ȸ���
			//�����µĹ�����ϵ
			copy.setSurvey(targSurvey);
			
			//�ֱ𱣴��µ�ҳ�������
			pageDao.saveEntity(copy);
			for(Question q : copy.getQuestions()){
				questionDao.saveEntity(q);
			}
			setOrderno(copy,targPage,pos);
		}
	}

	/**
	 * ����ҳ��
	 */
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//֮ǰ/֮��?
		if(pos == 0){
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			}
			else{
				Page prePage = getPrePage(targPage);
				srcPage.setOrderno((targPage.getOrderno() + prePage.getOrderno()) / 2);
			}
		}
		//֮��
		else{
			if(isLastPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			}
			else{
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((targPage.getOrderno() + nextPage.getOrderno()) / 2);
			}
		}
	}

	/**
	 * ���ҳ�����ڵ������һҳ
	 */
	private Page getPrePage(Page targPage) {
		String hql = "from Page p where p.orderno < ? and p.survey.id = ? order by p.orderno desc" ;
		return pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId()).get(0);
	}
	
   /**
	* ���ҳ�����ڵ������һҳ
	*/
	private Page getNextPage(Page targPage) {
		String hql = "from Page p where p.orderno > ? and p.survey.id = ? order by p.orderno asc" ;
		return pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId()).get(0);
	}

	/**
	 * �ж�ҳ���Ƿ������ڵ���βҳ
	 */
	private boolean isLastPage(Page targPage) {
		String hql = "from Page p where p.orderno > ? and p.survey.id = ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId());
		return !ValidateUtil.isValid(list);
	}

	/**
	 * �ж�ҳ���Ƿ������ڵ�����ҳ
	 */
	private boolean isFirstPage(Page targPage) {
		String hql = "from Page p where p.orderno < ? and p.survey.id = ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId());
		return !ValidateUtil.isValid(list);
	}
	
	/**
	 * ��ѯ���п��Բ���ĵ��� 
	 */
	public List<Survey> findAllAvailableSurveys(){
		String hql = "from Survey s where s.closed = ?" ;
		return surveyDao.findEntityByHQL(hql,false) ;
	}
	
	/**
	 * ��ѯ������ҳ
	 */
	public Page getFirstPage(Integer sid){
		String hql = "from Page p where p.survey.id = ? order by p.orderno asc" ;
		Page p = pageDao.findEntityByHQL(hql,sid).get(0);
		p.getQuestions().size();
		p.getSurvey().getTitle();
		return p ;
	}
	
	/**
	 * ��ѯָ��ҳ�����һҳ
	 */
	public Page getPrePage(Integer currPid){
		Page p = this.getPage(currPid);
		p =  this.getPrePage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 * ��ѯָ��ҳ�����һҳ
	 */
	public Page getNextPage(Integer currPid){
		Page p = this.getPage(currPid);
		p =  this.getNextPage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 * �����
	 */
	public void saveAnswers(List<Answer> list){
		String uuid = UUID.randomUUID().toString();
		Date date = new Date();
		for(Answer a : list){
			a.setUuid(uuid);
			a.setAnswerTime(date);
			answerDao.saveEntity(a);
		}
	}
	
	/**
	 * ��ѯ�������������
	 */
	public List<Question> getQuestions(Integer sid){
		String hql = "from Question q where q.page.survey.id = ?" ;
		return questionDao.findEntityByHQL(hql,sid);
	}
	
	/**
	 * ��ѯ��
	 */
	public List<Answer> findAnswers(Integer sid){
		String hql = "from Answer a where a.surveyId = ? order by a.uuid" ;
		return answerDao.findEntityByHQL(hql,sid);
	}
}

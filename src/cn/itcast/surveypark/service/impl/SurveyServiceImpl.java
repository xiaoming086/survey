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

	//注入surveyDao
	@Resource(name="surveyDao")
	private BaseDao<Survey> surveyDao ;
	
	//注入pageDao
	@Resource(name="pageDao")
	private BaseDao<Page> pageDao ;
	
	
	//注入questionDao
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao ;
	
	//注入answerDao
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao ;
	
	/**
	 * 新建调查 
	 */
	public Survey newSurvey(User u){
		Survey s = new Survey();
		Page p = new Page();
		
		//设置调查和user的关系
		s.setUser(u);
		p.setSurvey(s);
		s.getPages().add(p);
		
		//保存
		surveyDao.saveEntity(s);
		pageDao.saveEntity(p);
		return s ;
	}
	
	/**
	 * 查询指定用户调查集合
	 */
	public List<Survey> findMySurveys(User u){
		String hql = "from Survey s where s.user.id = ?" ;
		return surveyDao.findEntityByHQL(hql,u.getId());
	}
	

	/**
	 * 按照id查询调查
	 */
	public Survey getSurvey(Integer sid){
		return surveyDao.getEntity(sid); 
	}
	
	/**
	 * 按照id查询调查,携带所有孩子
	 */
	public Survey getSurveyWithChildren(Integer sid){
		Survey s = this.getSurvey(sid);
		//初始化问题集合和页面集合
		for(Page p : s.getPages()){
			p.getQuestions().size();
		}
		return s; 
	}
	
	/**
	 * 更新调查
	 */
	public void updateSurvey(Survey model){
		surveyDao.updateEntity(model);
	}
	

	/**
	 *  保存/更新页面
	 */
	public void saveOrUpdatePage(Page model){
		pageDao.saveOrUpdateEntity(model);
	}
	
	/**	
	 * 编辑页面标题
	 */
	public Page getPage(Integer pid){
		return pageDao.getEntity(pid);
	}
	
	/**
	 * 保存/更新问题
	 */
	public void saveOrUpdateQuestion(Question model){
		questionDao.saveOrUpdateEntity(model);
	}
	
	/**
	 * 编辑问题
	 */
	public Question getQuestion(Integer qid){
		return questionDao.getEntity(qid);
	}
	
	/**
	 * 删除问题
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
	 * 删除页面
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
	 * 删除调查 
	 */
	public void deleteSurvey(Integer sid){
		//1.answers
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);

		//2.questions
		//hibernate不支持两级以上的写操作.不能使用 delete xxx .. from Question q where q.page.survey.id = ?
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
	 * 清除答案
	 */
	public void clearAnswers(Integer sid){
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);
	}
	
	/**
	 * 切换状态 
	 */
	public void toggleStatus(Integer sid){
		Survey s = this.getSurvey(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql,!s.isClosed(),sid);
	}
	
	/**
	 * 更新数据库路径信息
	 */
	public void updateLogoPhotoPath(Integer sid, String path){
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql, path,sid);
	}
	
	/**
	 * 查询调查,携带page集合
	 */
	public List<Survey> findSurveysWithPage(User user){
		String hql = "from Survey s where s.user.id = ?"; 
		List<Survey> list = surveyDao.findEntityByHQL(hql,user.getId());
		//强行初始化页面集合
		for(Survey s: list){
			s.getPages().size();
		}
		return list ;
	}
	
	/**
	 * 进行移动和复制操作
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos){
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		//移动复制?
		if(srcSurvey.getId().equals(targSurvey.getId())){
			setOrderno(srcPage,targPage,pos);
		}
		//复制
		else{
			//对源页面进行深度复制
			srcPage.getQuestions().size();
			Page copy = (Page) DataUtil.deeplyCopy(srcPage) ; //对srcPage进行深度复制
			//设置新的关联关系
			copy.setSurvey(targSurvey);
			
			//分别保存新的页面和问题
			pageDao.saveEntity(copy);
			for(Question q : copy.getQuestions()){
				questionDao.saveEntity(q);
			}
			setOrderno(copy,targPage,pos);
		}
	}

	/**
	 * 设置页序
	 */
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//之前/之后?
		if(pos == 0){
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			}
			else{
				Page prePage = getPrePage(targPage);
				srcPage.setOrderno((targPage.getOrderno() + prePage.getOrderno()) / 2);
			}
		}
		//之后
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
	 * 获得页面所在调查的上一页
	 */
	private Page getPrePage(Page targPage) {
		String hql = "from Page p where p.orderno < ? and p.survey.id = ? order by p.orderno desc" ;
		return pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId()).get(0);
	}
	
   /**
	* 获得页面所在调查的上一页
	*/
	private Page getNextPage(Page targPage) {
		String hql = "from Page p where p.orderno > ? and p.survey.id = ? order by p.orderno asc" ;
		return pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId()).get(0);
	}

	/**
	 * 判断页面是否是所在调查尾页
	 */
	private boolean isLastPage(Page targPage) {
		String hql = "from Page p where p.orderno > ? and p.survey.id = ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId());
		return !ValidateUtil.isValid(list);
	}

	/**
	 * 判断页面是否是所在调查首页
	 */
	private boolean isFirstPage(Page targPage) {
		String hql = "from Page p where p.orderno < ? and p.survey.id = ?" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getOrderno(),targPage.getSurvey().getId());
		return !ValidateUtil.isValid(list);
	}
	
	/**
	 * 查询所有可以参与的调查 
	 */
	public List<Survey> findAllAvailableSurveys(){
		String hql = "from Survey s where s.closed = ?" ;
		return surveyDao.findEntityByHQL(hql,false) ;
	}
	
	/**
	 * 查询调查首页
	 */
	public Page getFirstPage(Integer sid){
		String hql = "from Page p where p.survey.id = ? order by p.orderno asc" ;
		Page p = pageDao.findEntityByHQL(hql,sid).get(0);
		p.getQuestions().size();
		p.getSurvey().getTitle();
		return p ;
	}
	
	/**
	 * 查询指定页面的上一页
	 */
	public Page getPrePage(Integer currPid){
		Page p = this.getPage(currPid);
		p =  this.getPrePage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 * 查询指定页面的下一页
	 */
	public Page getNextPage(Integer currPid){
		Page p = this.getPage(currPid);
		p =  this.getNextPage(p);
		p.getQuestions().size();
		return p ;
	}
	
	/**
	 * 保存答案
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
	 * 查询调查的所有问题
	 */
	public List<Question> getQuestions(Integer sid){
		String hql = "from Question q where q.page.survey.id = ?" ;
		return questionDao.findEntityByHQL(hql,sid);
	}
	
	/**
	 * 查询答案
	 */
	public List<Answer> findAnswers(Integer sid){
		String hql = "from Answer a where a.surveyId = ? order by a.uuid" ;
		return answerDao.findEntityByHQL(hql,sid);
	}
}

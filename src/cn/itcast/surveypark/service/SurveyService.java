package cn.itcast.surveypark.service;

import java.util.List;

import cn.itcast.surveypark.domain.Answer;
import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;

public interface SurveyService {

	/**
	 * �½����� 
	 */
	public Survey newSurvey(User u);

	/**
	 * ��ѯָ���û����鼯��
	 */
	public List<Survey> findMySurveys(User u);

	/**
	 * ����id��ѯ����
	 */
	public Survey getSurvey(Integer sid);

	
	/**
	 * ����id��ѯ����,Я�����к���
	 */
	public Survey getSurveyWithChildren(Integer sid);

	/**
	 * ���µ���
	 */
	public void updateSurvey(Survey model);

	/**
	 *  ����/����ҳ��
	 */
	public void saveOrUpdatePage(Page model);

	/**
	 * �༭ҳ�����
	 */
	public Page getPage(Integer pid);

	/**
	 * ����/��������
	 */
	public void saveOrUpdateQuestion(Question model);

	/**
	 * �༭����
	 */
	public Question getQuestion(Integer qid);

	/**
	 * ɾ������
	 */
	public void deleteQuestion(Integer qid);

	/**
	 * ɾ��ҳ��
	 */
	public void deletePage(Integer pid);

	/**
	 * ɾ������ 
	 */
	public void deleteSurvey(Integer sid);

	/**
	 * �����
	 */
	public void clearAnswers(Integer sid);

	/**
	 * �л�״̬ 
	 */
	public void toggleStatus(Integer sid);

	/**
	 * �������ݿ�·����Ϣ
	 */
	public void updateLogoPhotoPath(Integer sid, String path);

	/**
	 * ��ѯ����,Я��page����
	 */
	public List<Survey> findSurveysWithPage(User user);

	/**
	 * �����ƶ��͸��Ʋ���
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos);

	/**
	 * ��ѯ���п��Բ���ĵ��� 
	 */
	public List<Survey> findAllAvailableSurveys();

	/**
	 * ��ѯ������ҳ
	 */
	public Page getFirstPage(Integer sid);

	/**
	 * ��ѯָ��ҳ�����һҳ
	 */
	public Page getPrePage(Integer currPid);
	
	/**
	 * ��ѯָ��ҳ�����һҳ
	 */
	public Page getNextPage(Integer currPid);

	/**
	 * �����
	 */
	public void saveAnswers(List<Answer> processAnswers);

	/**
	 * ��ѯ�������������
	 */
	public List<Question> getQuestions(Integer sid);

	/**
	 * ��ѯ��
	 */
	public List<Answer> findAnswers(Integer sid);
}

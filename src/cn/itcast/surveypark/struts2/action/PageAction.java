package cn.itcast.surveypark.struts2.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.service.SurveyService;

/**
 * PageAction
 */
@Controller
@Scope("prototype")
public class PageAction extends BaseAction<Page> {

	private static final long serialVersionUID = -3941971081585995511L;

	private Integer sid ;
	
	private Integer pid ;
	
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Resource
	private SurveyService surveyService ;
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	/**
	 * ���ҳ��
	 */
	public String toAddPage(){
		return "addPagePage" ;
	}
	
	/**
	 * ����/����ҳ��
	 */
	public String saveOrUpdatePage(){
		//����model�͵������ϵ
		Survey s = new Survey();
		s.setId(sid);
		model.setSurvey(s);
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction" ;
	}
	
	/**
	 * �༭ҳ�����
	 */
	public String editPage(){
		this.model = surveyService.getPage(pid);
		return "editPagePage" ;
	}
	
	/**
	 * ɾ��ҳ��
	 */
	public String deletePage(){
		surveyService.deletePage(pid);
		return "designSurveyAction" ;
	}
}

package cn.itcast.surveypark.struts2.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Question;
import cn.itcast.surveypark.service.SurveyService;

/**
 * QuestionAction
 */
@Controller
@Scope("prototype")
public class QuestionAction extends BaseAction<Question> {

	private static final long serialVersionUID = -2703362769461607251L;
	
	private Integer sid ;
	
	private Integer pid ;
	
	private Integer qid ;
	
	public Integer getQid() {
		return qid;
	}
	public void setQid(Integer qid) {
		this.qid = qid;
	}

	@Resource
	private SurveyService surveyService ;
	
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	/**
	 * 到达选题型页面
	 */
	public String toSelectQuestionType(){
		return "selectQuestionTypePage" ;
	}
	
	/**
	 * 到达问题设计页面
	 */
	public String toDesignQuestionPage(){
		return "" + model.getQuestionType() ;
	}
	
	/**
	 * 保存/更新问题
	 */
	public String saveOrUpdateQuestion(){
		//维持关联关系
		Page p = new Page();
		p.setId(pid);
		model.setPage(p);
		surveyService.saveOrUpdateQuestion(model);
		return "designSurveyAction" ;
	}
	
	/**
	 * 编辑问题
	 */
	public String editQuestion(){
		this.model = surveyService.getQuestion(qid);
		return "" + model.getQuestionType() ;
	}
	
	/**
	 * 删除问题
	 */
	public String deleteQuestion(){
		surveyService.deleteQuestion(qid);
		return "designSurveyAction" ;
	}
}

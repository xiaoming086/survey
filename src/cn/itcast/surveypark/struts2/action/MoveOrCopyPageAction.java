package cn.itcast.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Page;
import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;

/**
 * MoveOrCopyPageAction
 */
@Controller
@Scope("prototype")
public class MoveOrCopyPageAction extends BaseAction<Page> implements UserAware{

	private static final long serialVersionUID = 4056112834637758144L;

	private Integer srcPid ;
	
	private Integer targPid ;
	
	//λ��:0-֮ǰ 1-֮��
	private int pos ;
	
	private Integer sid ;
	
	public Integer getTargPid() {
		return targPid;
	}
	public void setTargPid(Integer targPid) {
		this.targPid = targPid;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}


	private List<Survey> surveys ;
	
	@Resource
	private SurveyService surveyService ;

	//����user
	private User user;
	
	public List<Survey> getSurveys() {
		return surveys;
	}
	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}
	public Integer getSrcPid() {
		return srcPid;
	}
	public void setSrcPid(Integer srcPid) {
		this.srcPid = srcPid;
	}
	
	/**
	 * ����ѡ��Ŀ��ҳ
	 */
	public String toSelectTargetPage(){
		//Դҳ��id
		this.surveys = surveyService.findSurveysWithPage(user);
		return "moveOrCopyPageListPage" ;
	}
	
	/**
	 * �����ƶ��͸��Ʋ���
	 */
	public String doMoveOrCopyPage(){
		surveyService.moveOrCopyPage(srcPid,targPid,pos);
		return "designSurveyAction" ;
	}
	
	
	/**
	 * ע��user
	 */
	public void setUser(User user) {
		this.user = user ;
	}
}

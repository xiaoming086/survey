package cn.itcast.surveypark.struts2.action;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.Survey;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 *
 */
@Controller
@Scope("prototype")
public class SurveyAction extends BaseAction<Survey> implements UserAware,ServletContextAware {

	private static final long serialVersionUID = -5740692445093858396L;

	@Resource
	private SurveyService surveyService ;
	
	private List<Survey> mySurveys ;
	
	private Integer sid ;
	
	//����ҳ
	private String inputPage ;

	public String getInputPage() {
		return inputPage;
	}

	public void setInputPage(String inputPage) {
		this.inputPage = inputPage;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}
	
	//����session��user����
	private User user;
	
	public List<Survey> getMySurveys() {
		return mySurveys;
	}

	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}

	/*
	 * �½�����
	 */
	public String newSurvey(){
		this.model = surveyService.newSurvey(user); 
		return "designSurveyPage" ;
	}
	
	/**
	 * ��ѯ�ҵĵ���
	 */ 
	public String mySurveys(){
		this.mySurveys = surveyService.findMySurveys(user);
		return "mySurveyListPage" ;
	}

	/**
	 * ��Ƶ���
	 */
	public String designSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurveyPage" ;
	}
	
	/**
	 * �༭����
	 */
	public String editSurvey(){
		this.model = surveyService.getSurvey(sid);
		return "editSurveyPage" ;
	}
	
	/**
	 * ���µ���
	 */
	public String updateSurvey(){
		//�����ض���
		this.sid = model.getId();
		//ά��������ϵ
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction" ;
	}
	
	//ע��user����
	public void setUser(User user) {
		this.user = user ;
	}
	
	/**
	 * ɾ������ 
	 */
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * �����
	 */
	public String clearAnswers(){
		surveyService.clearAnswers(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * �л�״̬ 
	 */
	public String toggleStatus(){
		surveyService.toggleStatus(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * ��������logoҳ��
	 */
	public String toAddLogoPage(){
		return "addLogoPage" ;
	}
	
	//�ļ��ϴ�
	private File logoPhoto ;
	private String logoPhotoFileName ;

	//����ServletContext����
	private ServletContext sc;
	
	public File getLogoPhoto() {
		return logoPhoto;
	}

	public void setLogoPhoto(File logoPhoto) {
		this.logoPhoto = logoPhoto;
	}

	public String getLogoPhotoFileName() {
		return logoPhotoFileName;
	}

	public void setLogoPhotoFileName(String logoPhotoFileName) {
		this.logoPhotoFileName = logoPhotoFileName;
	}

	/**
	 * ���logo�ϴ�
	 */
	public String doAddLogo(){
		if(ValidateUtil.isValid(logoPhotoFileName)){
			//�ϴ��ļ�
			String dir = sc.getRealPath("/upload");
			long l = System.nanoTime();
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File newFile = new File(dir, l + ext);
			logoPhoto.renameTo(newFile);
			//�������ݿ�·����Ϣ
			surveyService.updateLogoPhotoPath(sid,"/upload/" + l + ext);
		}
		return "designSurveyAction" ;
	}
	
	//�÷�����doAddLogo֮ǰִ��
	public void prepareDoAddLogo(){
		inputPage = "/addLogo.jsp" ;
	}
	
	//ע��ServletContext
	public void setServletContext(ServletContext context) {
		this.sc = context ;
	}
	
	/**
	 * �ж�ͼƬ�Ƿ����
	 */
	public boolean logoPhotoExists(){
		String path = model.getLogoPhotoPath();
		if(ValidateUtil.isValid(path)){
			String realPath = sc.getRealPath(path);
			return new File(realPath).exists();
		}
		return false ;
	}
	
	/**
	 * ��������
	 */
	public String analyzeSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "analyzeSurveyListPage" ;
	}
}

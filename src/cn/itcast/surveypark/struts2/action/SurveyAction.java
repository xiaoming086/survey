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
	
	//错误页
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
	
	//接受session的user对象
	private User user;
	
	public List<Survey> getMySurveys() {
		return mySurveys;
	}

	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}

	/*
	 * 新建调查
	 */
	public String newSurvey(){
		this.model = surveyService.newSurvey(user); 
		return "designSurveyPage" ;
	}
	
	/**
	 * 查询我的调查
	 */ 
	public String mySurveys(){
		this.mySurveys = surveyService.findMySurveys(user);
		return "mySurveyListPage" ;
	}

	/**
	 * 设计调查
	 */
	public String designSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurveyPage" ;
	}
	
	/**
	 * 编辑调查
	 */
	public String editSurvey(){
		this.model = surveyService.getSurvey(sid);
		return "editSurveyPage" ;
	}
	
	/**
	 * 更新调查
	 */
	public String updateSurvey(){
		//用于重定向
		this.sid = model.getId();
		//维护关联关系
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction" ;
	}
	
	//注入user对象
	public void setUser(User user) {
		this.user = user ;
	}
	
	/**
	 * 删除调查 
	 */
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * 清除答案
	 */
	public String clearAnswers(){
		surveyService.clearAnswers(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * 切换状态 
	 */
	public String toggleStatus(){
		surveyService.toggleStatus(sid);
		return "findMySurveysAction" ;
	}
	
	/**
	 * 到达增加logo页面
	 */
	public String toAddLogoPage(){
		return "addLogoPage" ;
	}
	
	//文件上传
	private File logoPhoto ;
	private String logoPhotoFileName ;

	//接受ServletContext对象
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
	 * 完成logo上传
	 */
	public String doAddLogo(){
		if(ValidateUtil.isValid(logoPhotoFileName)){
			//上传文件
			String dir = sc.getRealPath("/upload");
			long l = System.nanoTime();
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File newFile = new File(dir, l + ext);
			logoPhoto.renameTo(newFile);
			//更新数据库路径信息
			surveyService.updateLogoPhotoPath(sid,"/upload/" + l + ext);
		}
		return "designSurveyAction" ;
	}
	
	//该方法在doAddLogo之前执行
	public void prepareDoAddLogo(){
		inputPage = "/addLogo.jsp" ;
	}
	
	//注入ServletContext
	public void setServletContext(ServletContext context) {
		this.sc = context ;
	}
	
	/**
	 * 判断图片是否存在
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
	 * 分析调查
	 */
	public String analyzeSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "analyzeSurveyListPage" ;
	}
}

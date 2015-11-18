package cn.itcast.surveypark.struts2.action;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.service.UserService;
import cn.itcast.surveypark.util.DataUtil;

/**
 * LoginAction
 */
@Controller
@Scope("prototype")
public class LoginAction extends BaseAction<User> implements SessionAware{

	private static final long serialVersionUID = 2463194904755350843L;
	
	@Resource
	private UserService userService ;
	
	@Resource
	private RightService rightService ;

	//����session��map
	private Map<String, Object> sessionMap;
	
	/**
	 * �����¼ҳ��
	 */
	public String toLoginPage(){
		return "loginPage" ;
	}
	
	//���е�¼
	public String doLogin(){
		return SUCCESS ;

	}

	/**
	 * �÷���ֻ��doLogin֮ǰ����
	 */
	public void validateDoLogin(){
		User user =userService.validateLoginInfo(model.getEmail(),DataUtil.md5(model.getPassword()));
		if(user == null){
			addActionError("email/password wrong");
		}
		else{
			
			//��ʼ��Ȩ���ܺ�����
			int maxRightPos = rightService.getMaxRightPos();
			user.setRightSum(new long[maxRightPos + 1]);
			//�����û���Ȩ���ܺ�
			user.calculateRightSum();
			//user--->session
			sessionMap.put("user", user);
		}
	}

	//ע��session��map
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0 ;
	}
}

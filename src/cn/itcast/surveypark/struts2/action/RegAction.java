package cn.itcast.surveypark.struts2.action;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.UserService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.ValidateUtil;

@Controller
@Scope("prototype")
public class RegAction extends BaseAction<User> {

	private static final long serialVersionUID = -6134513189257569056L;
	
	//确认密码
	private String confirmPassword ;
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	//注入userService
	@Resource
	private UserService userService ;
	
	/**
	 * 到达注册页面
	 */
	@SkipValidation
	public String toRegPage(){
		return "regPage" ;
	}
	
	/**
	 * 进行注册
	 */
	public String doReg(){
		model.setPassword(DataUtil.md5(model.getPassword()));
		userService.saveEntity(model);
		return SUCCESS ;
	}
	
	public void validate() {
		//1.非空
		if(!ValidateUtil.isValid(model.getEmail())){
			addFieldError("email", "email是必填项!");
		}
		if(!ValidateUtil.isValid(model.getPassword())){
			addFieldError("password", "password是必填项!");
		}
		if(!ValidateUtil.isValid(model.getNickName())){
			addFieldError("nickName", "nickName是必填项!");
		}
		if(this.hasErrors()){
			return ;
		}
		//2.密码一致性
		if(!model.getPassword().equals(confirmPassword)){
			addFieldError("password", "密码输入不一致!");
			return ;
		}
		//3.email是否占用
		boolean b = userService.isRegisted(model.getEmail());
		if(b){
			addFieldError("email", "邮箱已占用!");
		}
		
	}

}

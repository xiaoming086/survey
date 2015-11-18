package cn.itcast.surveypark.struts2.action;

import cn.itcast.surveypark.domain.User;

/**
 * 用户关注接口
 */
public interface UserAware {
	public void setUser(User user);
}

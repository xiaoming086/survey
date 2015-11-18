package cn.itcast.surveypark.service;

import cn.itcast.surveypark.domain.User;

public interface UserService extends BaseService<User> {

	/**
	 * 判断邮箱是否占用
	 */
	public boolean isRegisted(String email);

	/**
	 * 校验登录信息
	 */
	public User validateLoginInfo(String email, String md5);

	/**
	 * 修改用户授权
	 */
	public void updateAuthorize(User model, Integer[] ownRoleIds);

	/**
	 * 清除授权
	 */
	public void clearAuthorize(Integer userId);
}

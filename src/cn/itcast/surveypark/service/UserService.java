package cn.itcast.surveypark.service;

import cn.itcast.surveypark.domain.User;

public interface UserService extends BaseService<User> {

	/**
	 * �ж������Ƿ�ռ��
	 */
	public boolean isRegisted(String email);

	/**
	 * У���¼��Ϣ
	 */
	public User validateLoginInfo(String email, String md5);

	/**
	 * �޸��û���Ȩ
	 */
	public void updateAuthorize(User model, Integer[] ownRoleIds);

	/**
	 * �����Ȩ
	 */
	public void clearAuthorize(Integer userId);
}

package cn.itcast.surveypark.service;

import java.util.List;
import java.util.Set;

import cn.itcast.surveypark.domain.security.Right;

public interface RightService extends BaseService<Right> {

	public void saveOrUpdateRight(Right model);

	/**
	 * ����url׷��Ȩ��
	 */
	public void appendRightByURL(String url);

	/**
	 * ��������Ȩ��
	 */
	public void batchUpdateRights(List<Right> allRights);

	/**
	 * ��ѯ��ָ����Χ�е�Ȩ��
	 */
	public List<Right> findRightsInRange(Integer[] ids);

	/**
	 * ��ѯ����ָ����Χ�е�Ȩ��
	 */
	public List<Right> findRightsNotInRange(Set<Right> rights);

	/**
	 * ��ѯ���Ȩ��λ
	 */
	public int getMaxRightPos();

}

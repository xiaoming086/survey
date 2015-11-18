package cn.itcast.surveypark.dao;

import java.util.List;

/**
 * dao�ӿ�
 */
public interface BaseDao<T> {
	//д����
	public void saveEntity(T t);
	public void updateEntity(T t);
	public void saveOrUpdateEntity(T t);
	public void deleteEntity(T t);
	//����hql������ʵ��
	public void batchEntityByHQL(String hql,Object...objects);
	//ִ��ԭ����sql���
	public void executeSQL(String sql,Object...objects);
	
	public T getEntity(Integer id);
	public T loadEntity(Integer id);
	public List<T> findEntityByHQL(String hql,Object...objects);
	//��ֵ����(��ѯ������ҽ���һ����¼)
	public Object uniqueResult(String hql,Object...objects);
	//����sql��ѯ
	public List<T> findObjectBySQL(String sql,Object...objects);
	
}

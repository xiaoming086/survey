package cn.itcast.surveypark.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.service.BaseService;

/**
 * �����serviceʵ��,ר�����ڼ̳�
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
	
	private BaseDao<T> dao ;
	
	private Class<T> clazz ;
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	//ע��dao
	@Resource
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}

	public void saveEntity(T t) {
		dao.saveEntity(t);
	}

	public void updateEntity(T t) {
		dao.updateEntity(t);
	}

	public void saveOrUpdateEntity(T t) {
		dao.saveOrUpdateEntity(t);
	}

	public void deleteEntity(T t) {
		dao.deleteEntity(t);
	}

	public void batchEntityByHQL(String hql, Object... objects) {
		dao.batchEntityByHQL(hql, objects);
	}
	
	//ִ��ԭ����sql���
	public void executeSQL(String sql,Object...objects){
		dao.executeSQL(sql, objects);
	}

	public T getEntity(Integer id) {
		return dao.getEntity(id);
	}

	public T loadEntity(Integer id) {
		return dao.loadEntity(id);
	}

	public List<T> findEntityByHQL(String hql, Object... objects) {
		return dao.findEntityByHQL(hql, objects);
	}
	
	//��ֵ����(��ѯ������ҽ���һ����¼)
	public Object uniqueResult(String hql,Object...objects){
		return dao.uniqueResult(hql, objects);
	}

	//��ѯ����ʵ��
	public List<T> findAllEntities(){
		String hql = "from " + clazz.getSimpleName() ;
		return (List<T>) this.findEntityByHQL(hql);
	}
	
	//����sql��ѯ
	public List<T> findObjectBySQL(String sql,Object...objects){
		return dao.findObjectBySQL(sql, objects);
	}
}

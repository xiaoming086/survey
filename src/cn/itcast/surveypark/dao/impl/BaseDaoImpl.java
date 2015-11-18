package cn.itcast.surveypark.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import cn.itcast.surveypark.dao.BaseDao;

/**
 * dao基类实现
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	@Resource
	private SessionFactory sf ;
	
	private Class<T> clazz ;
	
	public BaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
		
	}

	public void saveEntity(T t) {
		sf.getCurrentSession().save(t);
	}

	public void updateEntity(T t) {
		sf.getCurrentSession().update(t);
	}

	public void saveOrUpdateEntity(T t) {
		sf.getCurrentSession().saveOrUpdate(t);
	}

	public void deleteEntity(T t) {
		sf.getCurrentSession().delete(t);
	}

	public void batchEntityByHQL(String hql, Object... objects) {
		Query q = sf.getCurrentSession().createQuery(hql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		q.executeUpdate();
	}
	
	//执行原生的sql语句
	public void executeSQL(String sql,Object...objects){
		SQLQuery q = sf.getCurrentSession().createSQLQuery(sql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		q.executeUpdate();
	}

	//get
	public T getEntity(Integer id) {
		return (T) sf.getCurrentSession().get(clazz, id);
	}

	//load
	public T loadEntity(Integer id) {
		return (T) sf.getCurrentSession().load(clazz, id);
	}
	
	public List<T> findEntityByHQL(String hql, Object... objects) {
		Query q = sf.getCurrentSession().createQuery(hql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		return q.list();
	}
	
	//单值检索(查询结果有且仅有一条记录)
	public Object uniqueResult(String hql,Object...objects){
		Query q = sf.getCurrentSession().createQuery(hql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		return q.uniqueResult();
	}
	
	//按照sql查询
	public List<T> findObjectBySQL(String sql,Object...objects){
		SQLQuery q = sf.getCurrentSession().createSQLQuery(sql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		//添加实体类,将sql查询的集合中,将对象数组组装成实体对象
		q.addEntity(clazz);
		return q.list();
	}

}

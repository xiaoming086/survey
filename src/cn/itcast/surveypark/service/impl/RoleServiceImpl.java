package cn.itcast.surveypark.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.security.Role;
import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.service.RoleService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.ValidateUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * roleService
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
		RoleService{
	
	@Resource
	private RightService rightService ;

	/**
	 * ��д�÷���,����ע��
	 */
	@Resource(name="roleDao")
	public void setDao(BaseDao<Role> dao) {
		super.setDao(dao);
	}
	
	/**
	 * ����/���½�ɫ
	 */
	public void saveOrUpdateRole(Role r, Integer[] ownRightIds){
		//ids��Ч,˵��û�и���ɫ�����κ�Ȩ��.
		if(!ValidateUtil.isValid(ownRightIds)){
			r.getRights().clear();
		}
		else{
			List<Right> rights = rightService.findRightsInRange(ownRightIds);
			r.setRights(new HashSet<Right>(rights));
		}
		this.saveOrUpdateEntity(r);
	}
	
	/**
	 * ��ѯ����ָ����Χ�еĽ�ɫ����
	 */
	public List<Role> findRolesNotInRange(Set<Role> roles){
		if(!ValidateUtil.isValid(roles)){
			return this.findAllEntities() ;
		}
		else{
			String hql = "from Role r where r.id not in (" + DataUtil.extractEntityIds(roles)+ ")" ;
			return this.findEntityByHQL(hql);
		}
	}
}

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
	 * 重写该方法,覆盖注解
	 */
	@Resource(name="roleDao")
	public void setDao(BaseDao<Role> dao) {
		super.setDao(dao);
	}
	
	/**
	 * 保存/更新角色
	 */
	public void saveOrUpdateRole(Role r, Integer[] ownRightIds){
		//ids无效,说明没有给角色授予任何权限.
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
	 * 查询不在指定范围中的角色集合
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

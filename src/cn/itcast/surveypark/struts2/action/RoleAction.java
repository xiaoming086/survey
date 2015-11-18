package cn.itcast.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.domain.security.Role;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.service.RoleService;

/**
 * RoleAction
 */
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	private static final long serialVersionUID = -5581335869443875482L;

	private List<Role> allRoles ;
	//角色没有的权限集合
	private List<Right> noOwnRights ;
	
	private Integer roleId ;
	
	public Integer getRoleId() {
		return roleId;
	}


	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	//接受授予的权限id数组
	private Integer[] ownRightIds ;
	
	public Integer[] getOwnRightIds() {
		return ownRightIds;
	}


	public void setOwnRightIds(Integer[] ownRightIds) {
		this.ownRightIds = ownRightIds;
	}

	@Resource
	private RoleService roleService ;
	
	@Resource
	private RightService rightService ;
	
	public List<Right> getNoOwnRights() {
		return noOwnRights;
	}


	public void setNoOwnRights(List<Right> noOwnRights) {
		this.noOwnRights = noOwnRights;
	}
	
	public List<Role> getAllRoles() {
		return allRoles;
	}


	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	/**
	 * 查询所有角色
	 */
	public String findAllRoles(){
		this.allRoles = roleService.findAllEntities() ;
		return "roleListPage" ;
	}
	
	/**
	 * 添加角色
	 */
	public String toAddRolePage(){
		this.noOwnRights = rightService.findAllEntities();
		return "addRolePage" ;
	}
	
	/**
	 * 保存/更新角色
	 */
	public String saveOrUpdateRole(){
		roleService.saveOrUpdateRole(model,ownRightIds);
		return "findAllRolesAction" ;
	}
	
	/**
	 * 编辑角色
	 */
	public String editRole(){
		this.model = roleService.getEntity(roleId);
		this.noOwnRights = rightService.findRightsNotInRange(model.getRights());
		return "editRolePage" ;
	}
	
	/**
	 * 删除角色
	 */
	public String deleteRole(){
		Role r = new Role();
		r.setId(roleId);
		roleService.deleteEntity(r);
		return "findAllRolesAction" ;
	}
}

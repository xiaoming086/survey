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
	//��ɫû�е�Ȩ�޼���
	private List<Right> noOwnRights ;
	
	private Integer roleId ;
	
	public Integer getRoleId() {
		return roleId;
	}


	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	//���������Ȩ��id����
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
	 * ��ѯ���н�ɫ
	 */
	public String findAllRoles(){
		this.allRoles = roleService.findAllEntities() ;
		return "roleListPage" ;
	}
	
	/**
	 * ��ӽ�ɫ
	 */
	public String toAddRolePage(){
		this.noOwnRights = rightService.findAllEntities();
		return "addRolePage" ;
	}
	
	/**
	 * ����/���½�ɫ
	 */
	public String saveOrUpdateRole(){
		roleService.saveOrUpdateRole(model,ownRightIds);
		return "findAllRolesAction" ;
	}
	
	/**
	 * �༭��ɫ
	 */
	public String editRole(){
		this.model = roleService.getEntity(roleId);
		this.noOwnRights = rightService.findRightsNotInRange(model.getRights());
		return "editRolePage" ;
	}
	
	/**
	 * ɾ����ɫ
	 */
	public String deleteRole(){
		Role r = new Role();
		r.setId(roleId);
		roleService.deleteEntity(r);
		return "findAllRolesAction" ;
	}
}

package cn.itcast.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.service.RightService;

/**
 * RightAction
 */
@Controller
@Scope("prototype")
public class RightAction extends BaseAction<Right> {

	private static final long serialVersionUID = 4030840515774739462L;

	private List<Right> allRights ;
	
	@Resource
	private RightService rightService ;
	
	private Integer rightId ;
	
	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	public List<Right> getAllRights() {
		return allRights;
	}

	public void setAllRights(List<Right> allRights) {
		this.allRights = allRights;
	}

	/**
	 * ��ѯȫ��Ȩ��
	 */
	public String findAllRights(){
		this.allRights = rightService.findAllEntities();
		return "rightListPage" ;
	}
	
	/**
	 * ���Ȩ��
	 */
	public String toAddRightPage(){
		return "addRightPage" ;
	}
	
	/**
	 * ����/����Ȩ��
	 */
	public String saveOrUpdateRight(){
		rightService.saveOrUpdateRight(model);
		return "findAllRightsAction" ;
	}
	
	/**
	 * �༭Ȩ��
	 */
	public String editRight(){
		this.model = rightService.getEntity(rightId);
		return "editRightPage" ;
	}
	
	/**
	 * ɾ��Ȩ��
	 */
	public String deleteRight(){
		Right r = new Right();
		r.setId(rightId);
		rightService.deleteEntity(r);
		return "findAllRightsAction" ;
	}
	
	/**
	 * ��������Ȩ��
	 */
	public String batchUpdateRights(){
		rightService.batchUpdateRights(allRights);
		return "findAllRightsAction" ;
	}
}

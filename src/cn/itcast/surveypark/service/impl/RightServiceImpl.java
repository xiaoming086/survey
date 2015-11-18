package cn.itcast.surveypark.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.StringUtil;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 * rightService
 */
@Service("rightService")
public class RightServiceImpl extends BaseServiceImpl<Right> implements
		RightService{

	/**
	 * ��д�÷���,����ע��
	 */
	@Resource(name="rightDao")
	public void setDao(BaseDao<Right> dao) {
		super.setDao(dao);
	}
	
	/**
	 * ����/����Ȩ��
	 */
	public void saveOrUpdateRight(Right model){
		//insert
		if(model.getId() == null){
			int rightPos = 0 ;
			long rightCode = 1 ;
			//��ѯ���Ȩ��λ�ϵ����Ȩ����
			String hql = "select max(r.rightPos),max(r.rightCode) from Right r " +
					"where r.rightPos = (select max(rr.rightPos) from Right rr)" ;
			Object[] arr = (Object[]) this.uniqueResult(hql);
			Integer topRightPos = (Integer) arr[0] ;
			Long topRightCode = (Long) arr[1];
			if(topRightPos == null){
				rightPos = 0 ;
				rightCode = 1 ;
			}
			else{
				if(topRightCode >= (1L << 60)){
					rightPos = topRightPos + 1;
					rightCode = 1 ;
				}
				else{
					rightPos = topRightPos ;
					rightCode = topRightCode << 1 ;
				}
			}
			model.setRightPos(rightPos);
			model.setRightCode(rightCode);
		}
		this.saveOrUpdateEntity(model);
	}
	
	/**
	 * ����url׷��Ȩ��
	 */
	public void appendRightByURL(String url){
		String hql = "select count(*) from Right r where r.rightUrl = ?" ;
		Long count = (Long)this.uniqueResult(hql,url);
		if(count == 0){
			Right r = new Right();
			r.setRightUrl(url);
			this.saveOrUpdateRight(r);
		}
	}
	

	/**
	 * ��������Ȩ��
	 */
	public void batchUpdateRights(List<Right> list){
		if(ValidateUtil.isValid(list)){
			String hql = "update Right r set r.rightName = ?,r.common = ? where r.id = ?" ;
			for(Right r : list){
				this.batchEntityByHQL(hql,r.getRightName(),r.isCommon(),r.getId());
			}
		}
	}
	
	/**
	 * ��ѯ��ָ����Χ�е�Ȩ��
	 */
	public List<Right> findRightsInRange(Integer[] ids){
		if(ValidateUtil.isValid(ids)){
			String hql = "from Right r where r.id in (" + StringUtil.arr2Str(ids) + ")" ;
			return this.findEntityByHQL(hql);
		}
		return null ;
	}
	

	/**
	 * ��ѯ����ָ����Χ�е�Ȩ��
	 */
	public List<Right> findRightsNotInRange(Set<Right> rights){
		if(!ValidateUtil.isValid(rights)){
			return this.findAllEntities() ;
		}
		else{
			String hql = "from Right r where r.id not in (" + DataUtil.extractEntityIds(rights) + ")" ;
			return this.findEntityByHQL(hql);
		}
	}
	
	/**
	 * ��ѯ���Ȩ��λ
	 */
	public int getMaxRightPos(){
		String hql ="select max(r.rightPos) from Right r" ;
		Integer max = (Integer) this.uniqueResult(hql);
		return max == null ? 0 : max ;
	}
}

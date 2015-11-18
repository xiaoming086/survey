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
	 * 重写该方法,覆盖注解
	 */
	@Resource(name="rightDao")
	public void setDao(BaseDao<Right> dao) {
		super.setDao(dao);
	}
	
	/**
	 * 保存/更新权限
	 */
	public void saveOrUpdateRight(Right model){
		//insert
		if(model.getId() == null){
			int rightPos = 0 ;
			long rightCode = 1 ;
			//查询最大权限位上的最大权限码
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
	 * 按照url追加权限
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
	 * 批量更新权限
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
	 * 查询在指定范围中的权限
	 */
	public List<Right> findRightsInRange(Integer[] ids){
		if(ValidateUtil.isValid(ids)){
			String hql = "from Right r where r.id in (" + StringUtil.arr2Str(ids) + ")" ;
			return this.findEntityByHQL(hql);
		}
		return null ;
	}
	

	/**
	 * 查询不在指定范围中的权限
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
	 * 查询最大权限位
	 */
	public int getMaxRightPos(){
		String hql ="select max(r.rightPos) from Right r" ;
		Integer max = (Integer) this.uniqueResult(hql);
		return max == null ? 0 : max ;
	}
}

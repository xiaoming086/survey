package cn.itcast.surveypark.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.domain.security.Role;
import cn.itcast.surveypark.service.RoleService;
import cn.itcast.surveypark.service.UserService;
import cn.itcast.surveypark.util.StringUtil;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 * UserServiceImpl
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements
		UserService {

	@Resource
	private RoleService roleService ;
	/**
	 * 重写该方法,覆盖注解
	 */
	@Resource(name="userDao")
	public void setDao(BaseDao<User> dao) {
		super.setDao(dao);
	}
	
	/**
	 * 判断邮箱是否占用
	 */
	public boolean isRegisted(String email){
		String hql = "from User u where u.email = ?" ;
		List<User> list = this.findEntityByHQL(hql, email);
		return ValidateUtil.isValid(list);
	}
	

	/**
	 * 校验登录信息
	 */
	public User validateLoginInfo(String email, String password){
		String hql = "from User u where u.email = ? and u.password = ?" ;
		List<User> list = this.findEntityByHQL(hql,email,password);
		return ValidateUtil.isValid(list)?list.get(0):null ;
	}
	
	/**
	 * 修改用户授权
	 */
	public void updateAuthorize(User r, Integer[] ids){
		User uu = this.getEntity(r.getId());
		if(!ValidateUtil.isValid(ids)){
			uu.getRoles().clear();
		}
		else{
			String hql = "from Role r where r.id in ("+StringUtil.arr2Str(ids)+")" ;
			List<Role> roles  = roleService.findEntityByHQL(hql);
			uu.setRoles(new HashSet<Role>(roles));
		}
	}
	

	/**
	 * 清除授权
	 */
	public void clearAuthorize(Integer userId){
		this.getEntity(userId).getRoles().clear();
	}
}

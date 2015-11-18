package cn.itcast.surveypark.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.domain.security.Role;

/**
 * User
 */
public class User extends BaseEntity {
	private static final long serialVersionUID = -8203567911331304856L;
	private Integer id;
	private String email;
	private String password;
	private String nickName;
	private Date regDate = new Date();
	//�Ƿ��ǳ�������Ա
	private boolean superAdmin ;
	
	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	//��ɫ����
	private Set<Role> roles = new HashSet<Role>();
	
	//�û�Ȩ���ܺ�
	private long[] rightSum ;

	public long[] getRightSum() {
		return rightSum;
	}

	public void setRightSum(long[] rightSum) {
		this.rightSum = rightSum;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	/**
	 * �����û���Ȩ���ܺ�
	 */
	public void calculateRightSum() {
		int pos = 0 ;
		long code = 0 ;
		for(Role role : roles){
			//�жϳ�������Ա
			if("-1".equals(role.getRoleValue())){
				this.superAdmin = true ;
				roles = null ;
				return ;
			}
			for(Right r : role.getRights()){
				pos = r.getRightPos() ;
				code = r.getRightCode() ;
				rightSum[pos] = rightSum[pos] | code ;
			}
		}
		roles = null ;
	}

	/**
	 * �ж��û��Ƿ���ָ����Ȩ��
	 */
	public boolean hasRight(Right r) {
		int pos = r.getRightPos();
		long code = r.getRightCode();
		long ret = rightSum[pos] & code ;
		return !(ret == 0);
	}
}

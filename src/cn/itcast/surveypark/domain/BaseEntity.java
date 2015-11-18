package cn.itcast.surveypark.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

/**
 * 实体的超类,专门用于继承
 */
public abstract class BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -588660264095646727L;

	public abstract Integer getId() ;

	public abstract void setId(Integer id);
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		String className = this.getClass().getSimpleName();
		buffer.append(className + "{");
		Field[] fs = this.getClass().getDeclaredFields();
		//
		try {
			Class ftype = null ;
			Object fvalue = null ;
			for(Field f : fs){
				ftype = f.getType();
				//是基本类型/简单类型
				if((ftype.isPrimitive()
						|| ftype == String.class
						|| ftype == Integer.class
						|| ftype == Long.class
						|| ftype == Date.class)
						&& !Modifier.isStatic(f.getModifiers())){
					f.setAccessible(true);
					fvalue = f.get(this);
					buffer.append(f.getName()+":" + fvalue +",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		buffer.append("}");
		return buffer.toString();
	}
}

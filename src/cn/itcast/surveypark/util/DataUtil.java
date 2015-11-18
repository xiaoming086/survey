package cn.itcast.surveypark.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Collection;

import cn.itcast.surveypark.domain.BaseEntity;

/**
 * 数据工具类
 */
public class DataUtil {
	
	/**
	 * 采用md5加密
	 */
	public static String md5(String src){
		try {
			StringBuffer buffer = new StringBuffer();
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] data = md.digest(src.getBytes());
			for(byte b : data){
				//高4位
				buffer.append(chars[(b >> 4) & 0x0F]);
				//低4位
				buffer.append(chars[b & 0x0F]);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	/**
	 * 深度复制,复制的整个对象图.
	 */
	public static Serializable deeplyCopy(Serializable src){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(src);
			oos.close();
			baos.close();
			byte[] data = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Serializable copy = (Serializable) ois.readObject();
			ois.close();
			bais.close();
			return copy ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	/**
	 * 抽取所有实体的id,构成字符串
	 */
	public static String extractEntityIds(Collection<? extends BaseEntity> c){
		if(!ValidateUtil.isValid(c)){
			return null ;
		}
		else{
			String temp = "" ;
			for(BaseEntity e : c){
				temp = temp + e.getId() + "," ;
			}
			return temp.substring(0, temp.length() - 1);
		}
	}
}

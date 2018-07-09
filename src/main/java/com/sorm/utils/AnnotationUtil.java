package com.sorm.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.sorm.core.annotation.Cascade;
import com.sorm.core.annotation.Table;
import com.sorm.core.annotation.TablePK;

/**
 * 注解相关工具类
 * 
 * @author fengli
 * @date 2014-7-10 下午5:50:16
 * 
 */
public class AnnotationUtil {

	/**
	 * 获取所有注解信息
	 * 
	 * @param className
	 * @return
	 */
	public static Annotation[] getAllAnnotaions(String className) {
		try {
			Annotation annos[] = Class.forName(className).getAnnotations();
			return annos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有注解信息
	 * 
	 * @param clazz
	 * @return
	 */
	public static Annotation[] getAllAnnotaions(Class<?> clazz) {
		try {
			Annotation annos[] = clazz.getAnnotations();
			return annos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断是否是实体类，必须都包含Table，Database注解，Entity专用注解，其他类尽量不要使用
	 * 如果是实体类，会设置table和database属性，并返回改EntityInfo实例
	 * 
	 * @param annotaions
	 * @return
	 */
	public static String hasTable(Class<?> c ) {
		Annotation[] annotations = c.getAnnotations();
		
		if (annotations != null && annotations.length > 0) {
			for (Annotation a : annotations) {
				if (a instanceof Table) {
					String table = ((Table)a).value();
					return table;
				}
			}
		}
		return null;
	}
	
	public static String hasTablePK(Class<?> c ) {
		Field[] fields = c.getDeclaredFields();
		for(Field field :fields){
			if(field.isAnnotationPresent(TablePK.class)){
				return field.getName();
			}
		}
		return null;
	}
	
	
	public static String hasCasede(Class<?> c ,String foreignKey ) {
		Field[] fields = c.getDeclaredFields();
		for(Field field :fields){
			if(field.isAnnotationPresent(Cascade.class)){
				Cascade cascade = field.getAnnotation(Cascade.class);
				String value = cascade.value();
				if(value != null && value.equals(foreignKey)){
					return field.getName();
				}
			}
		}
		return null;
	}
	
}

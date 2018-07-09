package com.sorm.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sorm.core.annotation.Cascade;



public class EntityUtil {
	
	
	public static String EMPTY = "";
	
	private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();
	static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
    }
	/**
	 * 反射得到实例对象的信息跟值
	 * 
	 * @param entity
	 * @return
	 */
	public static Map<String, Object> getEntityInfo(Object entity) {
		Class<?> c = entity.getClass();
		Field[] fs = c.getDeclaredFields();
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (int i = 0; i < fs.length; i++) {
			String name = fs[i].getName();
			if (!fs[i].isAnnotationPresent(Cascade.class)) {
				try {
					PropertyDescriptor descriptor = new PropertyDescriptor(name, c);
					Object value = descriptor.getReadMethod().invoke(entity);
					valuesMap.put(name, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return valuesMap;
	}
	
	public static List<String> getAllFiled(Object entity) {
		Class<?> c = entity.getClass();
		Field[] fs = c.getDeclaredFields();
		List<String> filedList = new ArrayList<String>();
		for (int i = 0; i < fs.length; i++) {
			String name = fs[i].getName();
			if (!fs[i].isAnnotationPresent(Cascade.class)) {
				filedList.add(name);
			}
		}
		return filedList;
	}
	
	public static Object getFiledValueByName(Object entity,String name) throws Exception {
		PropertyDescriptor descriptor = new PropertyDescriptor(name, entity.getClass());
		return descriptor.getReadMethod().invoke(entity);
	}
	
	/**
	 * 获取实体对象信息，排除null值
	 * @param entity
	 * @return
	 */
	public static Map<String, Object> getEntityInfoWithoutNULL(Object entity) {
		Class<?> c = entity.getClass();
		Field[] fs = c.getDeclaredFields();
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (int i = 0; i < fs.length; i++) {
			String name = fs[i].getName();
			if (!fs[i].isAnnotationPresent(Cascade.class)) {
				try {
					PropertyDescriptor descriptor = new PropertyDescriptor(name, c);
					Object value = descriptor.getReadMethod().invoke(entity);
					
					Class<?> propType = descriptor.getPropertyType();
					if(value != null){
						
						//基本属性默认值也排除
						if(!(propType.isPrimitive() && value.equals(primitiveDefaults.get(propType)))){
							valuesMap.put(name, value);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return valuesMap;
	}
	
	/**
	 * 获取列和值
	 * @param data
	 * @return
	 */
	public static Object[][] getColumnAndValue(Map<String, Object> data) {
		Object[][] result = new Object[2][];
		Object[] columns = new Object[data.size()];
		Object[] values = new Object[data.size()];
		Set<String> keys = data.keySet();
		int i = 0;
		for (String key : keys) {
			columns[i] = key;
			values[i] = data.get(key);
			i++;
		}
		result[0] = columns;
		result[1] = values;
		return result;
	}
	
	/**
	 * 获取列和值，排除null值
	 * @param data
	 * @return
	 */
	public static Map<String, Object>  getColumnAndValueWithoutNull(Map<String, Object> data) {
		Set<String> keys = data.keySet();
		Map<String, Object> result = new HashMap<String, Object>();
		for (String key : keys) {
			Object o = data.get(key);
			if (o != null) {
				result.put(key, o);
			}
		}
		return result;
	}
	
	/**
	 * 首字母大写
	 * @param s
	 * @return
	 */
	public static String getFirstUpperString(String s) {
		if (s == null || s.length() < 1) {
			return EMPTY;
		}
		String s1 = s.substring(0, 1);
		String s2 = s.substring(1);
		return s1.toUpperCase() + s2;
	}
}

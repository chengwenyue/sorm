package com.sorm.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sorm.core.annotation.Cascade;
import com.sorm.utils.EntityUtil;

public class DemoTest {


	@Test
	public void testDeclaredFields(){
		Class<User> c = User.class;
		Field[] fields = c.getDeclaredFields();
		for(Field field:fields){
			System.out.println(field.getName());
		}
		
		User demoBean = new User();
		
		Field[] fs = demoBean.getClass().getDeclaredFields();
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (int i = 0; i < fs.length; i++) {
			String name = fs[i].getName();
			if (!fs[i].isAnnotationPresent(Cascade.class)) {
				try {
					Method m = demoBean.getClass().getMethod("get" + getFirstUpperString(name), null);
					Object buf = m.invoke(demoBean, null);
					valuesMap.put(name, buf);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(valuesMap);
	}
	
	/**
	 * 首字母大写
	 * @param s
	 * @return
	 */
	public static String getFirstUpperString(String s) {
		if (s == null || s.length() < 1) {
			return "";
		}
		String s1 = s.substring(0, 1);
		String s2 = s.substring(1);
		return s1.toUpperCase() + s2;
	}
	
	@Test
	public void setProperty()throws Exception{
		User person = new User();
		person.setName("123");
        PropertyDescriptor propDesc= new PropertyDescriptor("name", person.getClass());
        Method methodSetUserName=propDesc.getWriteMethod();
        methodSetUserName.invoke(person, "wong");
        System.out.println("set userName:"+person.getName());
    }
	
	@Test
	public void setPropertyByIntrospector()throws Exception{
        BeanInfo beanInfo= Introspector.getBeanInfo(User.class);
        PropertyDescriptor[] proDescrtptors=beanInfo.getPropertyDescriptors();
        
        if(proDescrtptors!=null&&proDescrtptors.length>0){
            for(PropertyDescriptor propDesc:proDescrtptors){
                System.out.println(propDesc);
            }
        }
	}
	
	@Test
	public void testGetBeanInfo(){
		User user = new User();
		user.setName("");
		Map<String, Object> map = EntityUtil.getEntityInfoWithoutNULL(user);
		System.out.println(map);
		
//		try {
//			BeanInfo beanInfo = Introspector.getBeanInfo(user.getClass());
//			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
//			for(PropertyDescriptor prop :props){
//			}
//		} catch (IntrospectionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}

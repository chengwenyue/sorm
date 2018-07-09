package com.sorm.core;

import java.util.List;
import java.util.Map;

import com.sorm.core.grammer.SQLBuilder;

public interface BaseDao {
	public long save(Object entity) throws Exception;  
    
    public boolean update(Object entity)  throws Exception; 
    
    public boolean update(Object entity,String idName,Object idValue)throws Exception;  
    
    public <T> boolean update(Object entity,SQLBuilder sqlBuilder) throws Exception; 
    
    public <T> boolean delete(Class<T> entityClass,Object entityid) throws Exception;  
      
    public <T> boolean delete(Class<T> entityClass,Object[] entityids) throws Exception;  
    
    public <T> boolean delete(SQLBuilder sqlBuilder) throws Exception;  
      
    public <T> T find(Class<T> entityClass,Object entityid) throws Exception; 
    
    public <T> List<T> queryAllData(Class<T> entityClass) throws Exception;
    
    public long getCount(SQLBuilder sqlBuilder) throws Exception;
    
    public <T> List<T> getBeans(Class<T> c,SQLBuilder sqlBuilder )throws Exception ;
    
    public <T> T getBean(Class<T> c,SQLBuilder sqlBuilder) throws Exception;
    
    public List<Map<String, Object>> quary(SQLBuilder sqlBuilder) throws Exception;
    
    public List<Map<String, Object>> specialSQL(String sql ,Object... params) throws Exception;
    
    public <T> List<T> resetList(List<?> list,Class<T> c,String foreignKey)  throws Exception;
    
    public <T> List<T> resetList(List<?> list,Class<T> c,String foreignKey ,String foreignFiled)  throws Exception;
}

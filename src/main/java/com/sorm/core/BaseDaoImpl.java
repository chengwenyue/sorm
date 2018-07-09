package com.sorm.core;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.sorm.core.grammer.SQLBuilder;
import com.sorm.core.grammer.WhereBuilder;
import com.sorm.utils.AnnotationUtil;
import com.sorm.utils.EntityUtil;
import com.sorm.utils.TransactionManager;

public class BaseDaoImpl implements BaseDao{

	/**
	 * 保存一个对象
	 * @throws SQLException 
	 */
	public long save(Object entity) throws SQLException {
		String table = AnnotationUtil.hasTable(entity.getClass());
		if(table == null || "".equals(table)){
			throw new RuntimeException("实体'"+entity.getClass().getName()+"'未对应表");
		}
		SQLBuilder sqlBuilder = new SQLBuilder(table);
		String sql = sqlBuilder.createInsertSQL(EntityUtil.getEntityInfoWithoutNULL(entity));
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		Object[] result = queryRunner.insert(sql, new ArrayHandler(),sqlBuilder.getParameters().toArray());
		
		if(result != null && result.length > 0){
			return (Long) result[0];
		}
		return 0;
	}

	public boolean update(Object entity) throws Exception {

		String tablePK = AnnotationUtil.hasTablePK(entity.getClass());
		if(tablePK == null || "".equals(tablePK)){
			throw new RuntimeException("实体'"+entity.getClass().getName()+"'未对应主键");
		}
		Object pkValue = EntityUtil.getFiledValueByName(entity, tablePK);
		return update(entity,tablePK,pkValue);
	}
	
	public boolean update(Object entity, String idName,Object idValue) throws Exception {
		String table = AnnotationUtil.hasTable(entity.getClass());
		if(table == null || "".equals(table)){
			throw new RuntimeException("实体'"+entity.getClass().getName()+"'未对应表");
		}
		SQLBuilder sqlBuilder = new SQLBuilder(table);

		sqlBuilder.addWhereEQ(idName, idValue);
		
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		int update = queryRunner.update(sqlBuilder.createUpdateSQL(entity), sqlBuilder.getParameters().toArray());
		
		return update >= 1;
	}
	
	public <T> boolean update(Object entity, SQLBuilder sqlBuilder)
			throws Exception {
		if(!sqlBuilder.hasTable()){
			String table = AnnotationUtil.hasTable(entity.getClass());
			if(table == null || "".equals(table)){
				throw new RuntimeException("实体'"+entity.getClass().getName()+"'未对应表");
			}
			sqlBuilder.setTable(table);
		}
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		int update = queryRunner.update(sqlBuilder.createUpdateSQL(entity), sqlBuilder.getParameters().toArray());
		
		return update >= 1;
	}
	public <T> boolean delete(Class<T> entityClass, Object entityid) throws Exception {
		String table = AnnotationUtil.hasTable(entityClass);
		if(table == null || "".equals(table)){
			throw new RuntimeException("实体'"+entityClass.getName()+"'未对应表");
		}
		
		String tablePK = AnnotationUtil.hasTablePK(entityClass);
		if(tablePK == null || "".equals(tablePK)){
			throw new RuntimeException("实体'"+entityClass.getName()+"'未对应主键");
		}
		
		SQLBuilder sqlBuilder = new SQLBuilder(table);
		sqlBuilder.addWhereEQ(tablePK, entityid);
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		int update = queryRunner.update(sqlBuilder.createDeleteSQL(), sqlBuilder.getParameters().toArray());
		
		return update >= 1;
	}

	public <T> boolean delete(Class<T> entityClass, Object[] entityids) throws Exception {
		String table = AnnotationUtil.hasTable(entityClass);
		if(table == null || "".equals(table)){
			throw new RuntimeException("实体'"+entityClass.getName()+"'未对应表");
		}
		
		String tablePK = AnnotationUtil.hasTablePK(entityClass);
		if(tablePK == null || "".equals(tablePK)){
			throw new RuntimeException("实体'"+entityClass.getName()+"'未对应主键");
		}
		
		SQLBuilder sqlBuilder = new SQLBuilder(table);
		sqlBuilder.addWhereIN(tablePK, entityids);
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		int update = queryRunner.update(sqlBuilder.createDeleteSQL(), sqlBuilder.getParameters().toArray());
		return update >= 1;
		
	}

	public <T> boolean delete(SQLBuilder sqlBuilder) throws Exception {
		if(!sqlBuilder.hasTable()){
			throw new RuntimeException("查询未设置表名！");
		}
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		int update = queryRunner.update(sqlBuilder.createDeleteSQL(), sqlBuilder.getParameters().toArray());
		return update >= 1;
	}

	public <T> T find(Class<T> entityClass, Object entityid) throws Exception {
		String table = AnnotationUtil.hasTable(entityClass);
		if(table == null || "".equals(table)){
			throw new RuntimeException("实体'"+entityClass.getName()+"'未对应表");
		}
		
		String tablePK = AnnotationUtil.hasTablePK(entityClass);
		if(tablePK == null || "".equals(tablePK)){
			throw new RuntimeException("实体'"+entityClass.getName()+"'未对应主键");
		}
		SQLBuilder sqlBuilder = new SQLBuilder(table);
		sqlBuilder.addWhereEQ(tablePK, entityid);
		
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		T t = queryRunner.query(sqlBuilder.createSelectSQL(), new BeanHandler<T>(entityClass),sqlBuilder.getParameters().toArray());
		return t;
	}

	public <T> List<T> queryAllData(Class<T> entityClass) throws Exception {
		
		String table = AnnotationUtil.hasTable(entityClass);
		if(table == null || "".equals(table)){
			throw new RuntimeException("实体'"+entityClass.getName()+"'未对应表");
		}
		SQLBuilder sqlBuilder = new SQLBuilder(table);
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		List<T> list = queryRunner.query(sqlBuilder.createSelectSQL(), new BeanListHandler<T>(entityClass));
		return list;
	}


	public long getCount(SQLBuilder sqlBuilder) throws Exception {
		if(sqlBuilder == null){
			throw new NullPointerException();
		}
		sqlBuilder.addCount();
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		Map<String, Object> query = queryRunner.query(sqlBuilder.createSelectSQL(), 
				new MapHandler(),sqlBuilder.getParameters().toArray());
		if(query != null){
			Object object = query.get("count(*)");
			if(object != null){
				Long count = (Long)object ;
				return count;
			}
		}
		return 0;
	}

	public <T> List<T> getBeans(Class<T> c, SQLBuilder sqlBuilder) throws Exception {
		if(!sqlBuilder.hasTable()){
			String table = AnnotationUtil.hasTable(c);
			if(table == null || "".equals(table)){
				throw new RuntimeException("实体'"+c.getName()+"'未对应表");
			}
			sqlBuilder.setTable(table);
		}
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		List<T> list = queryRunner.query(sqlBuilder.createSelectSQL(),
				new BeanListHandler<T>(c),sqlBuilder.getParameters().toArray());
		return list;
	}

	public <T> T getBean(Class<T> c, SQLBuilder sqlBuilder) throws Exception {
		if(!sqlBuilder.hasTable()){
			String table = AnnotationUtil.hasTable(c);
			if(table == null || "".equals(table)){
				throw new RuntimeException("实体'"+c.getName()+"'未对应表");
			}
			sqlBuilder.setTable(table);
		}
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		T t = queryRunner.query(sqlBuilder.createSelectSQL(),
				new BeanHandler<T>(c),sqlBuilder.getParameters().toArray());
		return t;
	}
	
	public List<Map<String, Object>> quary(SQLBuilder sqlBuilder)
			throws Exception {
		if(!sqlBuilder.hasTable()){
			throw new RuntimeException("查询未设置表名！");
		}
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		List<Map<String,Object>> list = queryRunner.query(sqlBuilder.createSelectSQL(), new MapListHandler(),sqlBuilder.getParameters().toArray());
		return list;
	}
	
	public List<Map<String, Object>> specialSQL(String sql,Object... params) throws Exception {
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		List<Map<String,Object>> list = queryRunner.query(sql, new MapListHandler(), params);
		return list;
	}

	public <T> List<T> resetList(List<?> list, Class<T> c, String foreignKey) throws Exception {
		if(list== null ||list.size() < 0){
			return null;
		}
		
		
		String tableT = AnnotationUtil.hasTable(c);
		if(tableT == null || "".equals(tableT)){
			throw new RuntimeException("实体'"+c.getName()+"'未对应表");
		}
		
		String tPK= AnnotationUtil.hasTablePK(c);
		if(tPK == null || "".equals(tPK)){
			throw new RuntimeException("实体'"+c.getName()+"'未对应主键");
		}
		
		Class<?> o = list.get(0).getClass();
		String foreignFiled = AnnotationUtil.hasCasede(o, foreignKey);
		if(foreignFiled == null || "".equals(foreignFiled)){
			throw new RuntimeException("实体'"+o.getName()+"'中字段 ："+foreignKey+"无级联");
		}
		
		
		SQLBuilder sqlBuilder = new SQLBuilder(tableT);
		sqlBuilder.addWhereEQ(tPK, "");
		List<T> tList = new ArrayList<T>();
		Map<Object ,T> map = new HashMap<Object, T>();
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		for(Object entity : list){
			
			Object valueByName = EntityUtil.getFiledValueByName(entity, foreignKey);
			
			PropertyDescriptor descriptor = new PropertyDescriptor(foreignFiled, o);
			Method method = descriptor.getWriteMethod();
			if(map.get(valueByName) != null){
				method.invoke(entity, map.get(valueByName));
			}else{
				T t = queryRunner.query(sqlBuilder.createSelectSQL(),new BeanHandler<T>(c), valueByName);
				method.invoke(entity, t);
				map.put(valueByName, t);
				tList.add(t);
			}
		}
		return tList;
	}

	public <T> List<T> resetList(List<?> list, Class<T> c, String foreignKey,
			String foreignFiled) throws Exception {
		
		if(list== null ||list.size() < 0){
			return null;
		}
		
		
		String tableT = AnnotationUtil.hasTable(c);
		if(tableT == null || "".equals(tableT)){
			throw new RuntimeException("实体'"+c.getName()+"'未对应表");
		}
		
		String tPK= AnnotationUtil.hasTablePK(c);
		if(tPK == null || "".equals(tPK)){
			throw new RuntimeException("实体'"+c.getName()+"'未对应主键");
		}
		
		
		SQLBuilder sqlBuilder = new SQLBuilder(tableT);
		sqlBuilder.addWhereEQ(tPK, "");
		List<T> tList = new ArrayList<T>();
		Map<Object ,T> map = new HashMap<Object, T>();
		QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
		for(Object entity : list){
			
			Object valueByName = EntityUtil.getFiledValueByName(entity, foreignKey);
			
			PropertyDescriptor descriptor = new PropertyDescriptor(foreignFiled, entity.getClass());
			Method method = descriptor.getWriteMethod();
			if(map.containsKey(valueByName)){
				method.invoke(entity, map.get(valueByName));
			}else{
				T t = queryRunner.query(sqlBuilder.createSelectSQL(),new BeanHandler<T>(c), valueByName);
				method.invoke(entity, t);
				map.put(valueByName, t);
				if(t != null)
					tList.add(t);
			}
		}
		return tList;
	}

}

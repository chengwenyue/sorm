package com.sorm.core;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Before;
import org.junit.Test;

import com.sorm.core.grammer.FiledBuilder;
import com.sorm.core.grammer.SQLBuilder;
import com.sorm.utils.DBUtil;
import com.sorm.utils.TransactionManager;

public class DaoTest {
	
	
	@Before
	public void before() throws IOException {
		DataSource dataSource = DBUtil.initDruidDataSource("jdbc.properties");
		TransactionManager.setSource(dataSource);
	}
	
	@Test
	public void testQuary(){
		try {
			QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
			List<Map<String,Object>> list = queryRunner.query("SELECT * FROM KMS.T_MENUHX_BAK", new MapListHandler());
			for(Map<String,Object> map:list )
				System.out.println(map);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQuaryRunnerInsert(){
		try {
			QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
			Map<String,Object> map = (Map<String, Object>) queryRunner.insert("insert into user values(?,?,?,?)", new MapHandler(),new Object[]{null,"cwy3",new Date(),1});
			System.out.println(map);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQuaryRunner(){
		try {
			SQLBuilder builder = new SQLBuilder("user");
			
//			filed.addFiled(new User());
//			filed.addCount("num");
			
//			builder.addWhereNOTIN("id", "21","13");
			builder.addWhereBETWEEN("birthday", "2017-05-14 23:12:30", "2017-05-15 21:06:01");
//			builder.addPqge(2);
			builder.addOrderDESC("birthday");
			builder.addGroup("name");
			
			String sql = builder.createSelectSQL();
			
			
			QueryRunner queryRunner = new QueryRunner(TransactionManager.getSource());
			List<User> users = queryRunner.query(sql,new BeanListHandler<User>(User.class),
					builder.getParameters().toArray());
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(User user :users){
				String format = sf.format(user.getBirthday());
				System.out.println(format);
				System.out.println(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSave(){
		BaseDao baseDao = new BaseDaoImpl();
		User user = new User();
		user.setName("cwy2");
		user.setBirthday(new Date());
		
		try {
			long i = baseDao.save(user);
			System.out.println(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testUpdate(){
		BaseDao baseDao = new BaseDaoImpl();
		User user = new User();
		user.setName("cwy");
		user.setId(21L);
		user.setBirthday(new Date());
		
		try {
			baseDao.update(user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testDelete(){
		BaseDao baseDao = new BaseDaoImpl();
		
		try {
			baseDao.delete(User.class, 26L);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testDeleteMany(){
		BaseDao baseDao = new BaseDaoImpl();
		
		try {
			baseDao.delete(User.class, new Object[]{22L,26L});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testFind(){
		BaseDao baseDao = new BaseDaoImpl();
		
		try {
			User user = baseDao.find(User.class, 22L);
			System.out.println(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testQueryAllData(){
		BaseDao baseDao = new BaseDaoImpl();
		try {
			List<User> list = baseDao.queryAllData(User.class);
			if(list != null){
				for(User user :list)
					System.out.println(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetCount(){
		BaseDao baseDao = new BaseDaoImpl();
		try {
			SQLBuilder sqlBuilder = new SQLBuilder("user");
//			sqlBuilder.addFiled("id");
			sqlBuilder.addCount("num");
//			sqlBuilder.addWhereIN("id", 21L,24L,23L);
			long count = baseDao.getCount(sqlBuilder);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testResetBeans(){
		BaseDao baseDao = new BaseDaoImpl();
		try {
			SQLBuilder sqlBuilder = new SQLBuilder("user");
			
			List<User> list = baseDao.getBeans(User.class, sqlBuilder);
			for(User user :list){
				System.out.println(user);
			}
			
			
			List<Dept> deptList = baseDao.resetList(list, Dept.class, "deptId","dept");
			
			for(User user :list){
				System.out.println(user);
			}
			for(Dept dept :deptList){
				System.out.println(dept);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

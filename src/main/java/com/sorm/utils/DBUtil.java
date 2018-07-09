package com.sorm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtil {
	private DBUtil() {}
	private static DataSource dataSource = null;
	public static DataSource initDruidDataSource(String path) throws IOException {
		if(dataSource != null)
			return dataSource;
		DruidDataSource druidDataSource = new DruidDataSource();
		Properties properties = readProperties(path);
		
//		BeanUtils.copyProperty(druidDataSource, "", value);
		druidDataSource.setDriverClassName(properties.getProperty("jdbc.driver"));
		druidDataSource.setUrl(properties.getProperty("jdbc.url"));
		druidDataSource.setUsername(properties.getProperty("jdbc.username"));
		druidDataSource.setPassword(properties.getProperty("jdbc.password"));
		
		druidDataSource.setInitialSize(Integer.valueOf(properties.getProperty("druid.initialSize")));
		druidDataSource.setMinIdle(Integer.valueOf(properties.getProperty("druid.minIdle")));
		druidDataSource.setMaxActive(Integer.valueOf(properties.getProperty("druid.maxActive")));
		druidDataSource.setMaxWait(Integer.valueOf(properties.getProperty("druid.maxWait")));
		druidDataSource.setTimeBetweenEvictionRunsMillis(Integer.valueOf(
				properties.getProperty("druid.timeBetweenEvictionRunsMillis")));
		druidDataSource.setMinEvictableIdleTimeMillis(Integer.valueOf(
				properties.getProperty("druid.minEvictableIdleTimeMillis")));
		
		return druidDataSource;
	}
	
	
	public static DataSource initC3p0DataSource(String path) throws Exception {
		if(dataSource != null)
			return dataSource;
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		Properties properties = readProperties(path);
		
//		BeanUtils.copyProperty(druidDataSource, "", value);
		comboPooledDataSource.setDriverClass(properties.getProperty("jdbc.driver"));
		comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
		comboPooledDataSource.setUser(properties.getProperty("jdbc.username"));
		comboPooledDataSource.setPassword(properties.getProperty("jdbc.password"));
		
		return comboPooledDataSource;
	}
	
	/**
	 * 从类加载路径下读取properties文件
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	private static Properties readProperties(String path) throws IOException {
		Properties properties = new Properties();
	    // 使用ClassLoader加载properties配置文件生成对应的输入流
	    InputStream in = DBUtil.class.getClassLoader().getResourceAsStream(path);
	    // 使用properties对象加载输入流
	    properties.load(in);
	    return properties;
	}
}

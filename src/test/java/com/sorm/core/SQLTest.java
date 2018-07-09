package com.sorm.core;

import org.junit.Test;

import com.sorm.core.grammer.FiledBuilder;
import com.sorm.core.grammer.SQLBuilder;

public class SQLTest {
	
	@Test
	public void testSQLFiled(){
		SQLBuilder builder = new SQLBuilder("user");
		FiledBuilder filed = new FiledBuilder(builder);
		
//		filed.addFiled(new User());
//		filed.addCount("num");
		
		
		String sql = builder.createSelectSQL();
		System.out.println(sql);
		
	}
	
	@Test
	public void testSQLWhere(){
		SQLBuilder builder = new SQLBuilder("user");
		FiledBuilder filed = new FiledBuilder(builder);
		
//		filed.addFiled(new User());
//		filed.addCount("num");
		
		builder.addWhereEQ("id", "11");
		
		String sql = builder.createSelectSQL();
		System.out.println(sql);
		
	}
}

package com.sorm.core.grammer;

import java.util.ArrayList;
import java.util.List;


public class HavingBuilder extends GrammarBuilder{
	private final List<QueryCondition> condtions = new ArrayList<QueryCondition>();
	
	private String mode;
	
	@Override
	public void resetSQL(SQLBuilder sqlBuilder) {
		StringBuffer sql = sqlBuilder.getSql();
		List<Object> parameters = sqlBuilder.getParameters();
		
		int size = condtions.size();
		int index = 0;
		String spliter = "";
		if (mode != null && mode.equals("or")) {
			spliter = " or ";
		} else {
			spliter = " and ";
		}
		if (size > 0) {
			for (QueryCondition c : condtions) {
				sql.append(c.toSql());
				parameters.addAll(c.getValues());
				if (index < (size - 1)) {
					sql.append(spliter);
				}
				index++;
			}
		}
	}
	
	/**
	 * 构造函数，指定组装语句类型，AND/OR
	 * @param mode
	 */
	public HavingBuilder(String mode ,SQLBuilder sqlBuilder) {
		super(sqlBuilder);
		sqlBuilder.setHavingBuilder(this);
		this.mode = mode;
	}
	
	/**
	 * 构造函数，指定组装语句类型，AND/OR
	 * @param mode
	 */
	public HavingBuilder(SQLBuilder sqlBuilder) {
		super(sqlBuilder);
		sqlBuilder.setHavingBuilder(this);
	}
	
	public void setQueryMode(String mode) {
		this.mode = mode;
	}
	/**
	 * 添加查询条件
	 * 调用方法：add(QueryCondition.EQ(column, 1) ... 
	 * @param condition
	 * @return
	 */
	public HavingBuilder add(QueryCondition condition) {
		condtions.add(condition);
		return this;
	}
	
}

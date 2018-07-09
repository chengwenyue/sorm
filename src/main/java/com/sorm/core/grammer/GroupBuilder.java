package com.sorm.core.grammer;

import java.util.Set;
import java.util.TreeSet;


public class GroupBuilder extends GrammarBuilder {
	
	public GroupBuilder(SQLBuilder sqlBuilder) {
		super(sqlBuilder);
		sqlBuilder.setGroupBuilder(this);
	}

	// 字段不能重复
	private final Set<String> columns = new TreeSet<String>();

	/**
	 * 选择分组，顺序和添加的顺序一致
	 * @param columns
	 * @return
	 */
	public GroupBuilder selectColumn(String columns) {
		this.columns.add(columns);
		return this;
	}
	

	@Override
	public void resetSQL(SQLBuilder sqlBuilder) {
		StringBuffer sql = sqlBuilder.getSql();
		int size = columns.size();
		int index = 0;
		if (size > 0) {
			for (String c : columns) {
				sql.append(c);
				if (index < (size - 1)) {
					sql.append(",");
				}
				index++;
			}
		}
	}
}

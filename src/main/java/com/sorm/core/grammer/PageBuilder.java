package com.sorm.core.grammer;

import java.util.List;

public class PageBuilder extends GrammarBuilder {
	
	private int startIndex;
	private int pageSize;
	
	
	public PageBuilder(SQLBuilder sqlBuilder,int startIndex, int pageSize) {
		super(sqlBuilder);
		sqlBuilder.setPageBuilder(this);
		this.startIndex = startIndex;
		this.pageSize = pageSize;
	}
	public PageBuilder(SQLBuilder sqlBuilder) {
		super(sqlBuilder);
		sqlBuilder.setPageBuilder(this);
	}
	

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	@Override
	public void resetSQL(SQLBuilder sqlBuilder) {
		StringBuffer sql = sqlBuilder.getSql();
		List<Object> parameters = sqlBuilder.getParameters();
		sql.append(" ?,? ");
		parameters.add(startIndex);
		parameters.add(pageSize);
		
	}

}

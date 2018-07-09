package com.sorm.core.grammer;

import java.util.ArrayList;
import java.util.List;


public abstract class GrammarBuilder {
	protected SQLBuilder sqlBuilder;
	public GrammarBuilder(SQLBuilder sqlBuilder){
		this.sqlBuilder = sqlBuilder;
	}
	public abstract void resetSQL(SQLBuilder sqlBuilder);
}

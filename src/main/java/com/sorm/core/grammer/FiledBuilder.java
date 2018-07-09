package com.sorm.core.grammer;

import java.util.HashMap;
import java.util.Map;

import com.sorm.utils.EntityUtil;

public class FiledBuilder extends GrammarBuilder{

	public FiledBuilder(SQLBuilder sqlBuilder) {
		super(sqlBuilder);
		sqlBuilder.setFiledBuilder(this);
	}

	private final Map<String ,String> fileds = new HashMap<String,String>();
	private final Map<String ,String> func = new HashMap<String, String>();

	@Override
	public void resetSQL(SQLBuilder sqlBuilder) {
		StringBuffer sql = sqlBuilder.getSql();
		if(fileds.size() > 0){
			for(Map.Entry<String, String> entry : fileds.entrySet()){
				sql.append(entry.getKey());
				if(entry.getValue() != null && !entry.getValue().equals("")){
					sql.append(" as ")
					.append(entry.getValue());
				}
				sql.append(",");
			}
		}
		
		if(sqlBuilder.isHasGroup() || fileds.size() == 0){
			for(Map.Entry<String, String> entry : func.entrySet()){
				sql.append(entry.getKey());
				if(entry.getValue() != null && !entry.getValue().equals("")){
					sql.append(" as ")
					.append(entry.getValue());
				}
				sql.append(",");
			}
		}
		if(fileds.size() == 0 && func.size() == 0){
			sql.append(" * ");
		}
		sql.deleteCharAt(sql.length() -1);
	}

	public FiledBuilder addFiled(String filed ,String alias){
		fileds.put(filed, alias);
		return this;
	}
	
	public FiledBuilder addFiled(String filed ){
		fileds.put(filed, null);
		return this;
	}
	
	public FiledBuilder addFiled(String... fileds ){
		for(String filed :fileds)
			this.fileds.put(filed, null);
		return this;
	}
	
	public FiledBuilder addFiled(Object entity){
		fileds.clear();
		for(String filed :EntityUtil.getAllFiled(entity)){
			addFiled(filed);
		}
		return this;
	}
	
	public FiledBuilder addCount(String countAlias){
		func.put("count(*)", countAlias);
		return this;
	}
	
	public FiledBuilder addCount(){
		return addCount(null);
	}
	public FiledBuilder addSum(String filed){
		return addSum(filed, null);
	}
	
	public FiledBuilder addSum(String filed,String sumAlias){
		func.put("sum("+filed+")", sumAlias);
		return this;
	}
}
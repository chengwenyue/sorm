package com.sorm.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLBuild {
	public static final String SELECT="select";
	public static final String UPDATE="update";
	public static final String DELETE="delete";

	public static final String gt =">";
	public static final String lt ="<";
	public static final String gte =">=";
	public static final String lte ="<=";
	public static final String ne ="<>";
	public static final String eq ="=";
	public static final String like ="like";
	
	public String opreate;
	public String orderBy;
	public String tableName;
	public String rule;
	public int index = 0;
	public Map<String, String> whereArgMap = new HashMap<String, String>();
	public Map<String, Object> valueMap = new HashMap<String, Object>();
	
	public Object[] params;
	public SQLBuild(String opreate ,String tableName) {
		this.opreate = opreate;
		this.tableName = tableName;
	}
	public SQLBuild(String tableName) {
		this.tableName = tableName;
		this.opreate = SELECT;
	}
	
	public SQLBuild addWhereArg(String key,String arg,Object value) {
		if(whereArgMap.containsKey(key+index) && valueMap.get(key+index).equals(value)){
			return this;
		}else{
			whereArgMap.put(key+index, key + " "+arg+" :" + key+index);
		}
//		whereArgMap.put(key, key + " "+arg+" :" + key);
		valueMap.put(key+index, value);
		index++;
		return this;
	}
	public SQLBuild addWhereIn(String key ,List t){
		addWhereIn(key, t, true);
		return this;
	}
	public SQLBuild addWhereIn(String key ,List t,boolean isIn){
		if(whereArgMap.containsKey(key+index))
			return this;
		StringBuffer buffer = new StringBuffer();
		if(isIn){
			buffer.append(key+" in (");
		}else{
			buffer.append(key+" not in (");
		}
		for(int i =0;i<t.size();i++){
			buffer.append(" :"+key+"_in_"+i).append(",");
			valueMap.put(key+"_in_"+i, t.get(i));
		}
		String in = buffer.substring(0, buffer.length()-1);
		in+=")";
		whereArgMap.put(key+index, in);
		index++;
		return this;
	}
	public SQLBuild addWhereIn(String key ,Object[] t,boolean isIn){
		if(whereArgMap.containsKey(key+index))
			return this;
		StringBuffer buffer = new StringBuffer();
		if(isIn){
			buffer.append(key+" in (");
		}else{
			buffer.append(key+" not in (");
		}
		for(int i =0;i<t.length;i++){
			buffer.append(" :"+key+"_in_"+i).append(",");
			valueMap.put(key+"_in_"+i, t[i]);
		}
		String in = buffer.substring(0, buffer.length()-1);
		in+=")";
		whereArgMap.put(key+index, in);
		index++;
		return this;
	}
	
	public SQLBuild addWhereIn(String key ,Object[] t){
		addWhereIn(key, t, true);
		return this;
	}
	public SQLBuild setOrderBy(String key ,String rule){
		orderBy = key;
		this.rule =rule;
		return this;
	}
	public SQLBuild setOrderBy(String key){
		orderBy = key;
		rule ="desc";
		return this;
	}
	public String createSQL(String opreate){
		this.opreate = opreate;
		String sql = null;
		StringBuilder builder = new StringBuilder();
		builder.append(opreate);
		if(SELECT.equals(opreate)){
			builder.append(" *");
		}
		builder.append(" from ").append(tableName);
		
		if(whereArgMap.size() != 0){
			builder.append(" where ");
			for(Map.Entry<String, String> entry : whereArgMap.entrySet()){
				builder.append(entry.getValue()+" and ");
			}
			
			sql = builder.substring(0, builder.length() - 5);
			
		}else{
			sql = builder.toString();
		}
		
		if(null != orderBy && orderBy.length() != 0){
			sql += " order by "+orderBy +" "+rule;
		}
		return sql;
		
	}
	
	public String createSQL(){
		String sql = null;
		StringBuilder builder = new StringBuilder();
		builder.append(opreate);
		if(SELECT.equals(opreate)){
			builder.append(" *");
		}
		builder.append(" from ").append(tableName);
		
		if(whereArgMap.size() != 0){
			builder.append(" where ");
			for(Map.Entry<String, String> entry : whereArgMap.entrySet()){
				builder.append(entry.getValue()+" and ");
			}
			
			sql = builder.substring(0, builder.length() - 5);
			
		}else{
			sql = builder.toString();
		}
		
		if(null != orderBy && orderBy.length() != 0){
			sql += " order by "+orderBy +" "+rule;
		}
		return sql;
		
	}
	
	public String createCountSQL(){
		String sql = null;
		StringBuilder builder = new StringBuilder();
	
		builder.append("select count(*) as num");
		builder.append(" from ").append(tableName);
		
		if(whereArgMap.size() != 0){
			builder.append(" where ");
			for(Map.Entry<String, String> entry : whereArgMap.entrySet()){
				builder.append(entry.getValue()+" and ");
			}
			
			sql = builder.substring(0, builder.length() - 5);
			
		}else{
			sql = builder.toString();
		}
		return sql;
	}
	
	public String createInsertSQL(Map<String, Object> map){
		int index = 0;
		StringBuffer buffer = new StringBuffer();
		buffer.append("insert into ")
		.append(tableName);
		StringBuilder fields = new StringBuilder(" (");
		StringBuilder values = new StringBuilder("");
		params = new Object[map.size()];
		
		for(Map.Entry<String, Object> entry :map.entrySet()){
			
			fields.append(entry.getKey()).append(",");
			params[index] = entry.getValue();
			values.append("?").append(",");
			index ++;
		}
		
		buffer.append(fields.substring(0,fields.length()-1));
		buffer.append(") values (");
		buffer.append(values.substring(0,values.length()-1));
		buffer.append(")");
		return buffer.toString();
	}
}

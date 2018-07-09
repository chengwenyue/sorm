package com.sorm.core.grammer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sorm.core.grammer.QueryCondition.Type;
import com.sorm.utils.EntityUtil;

/**
 * 构建SQL语句的核心类
 * @author cwy-pc
 *
 */
public class SQLBuilder {
	private StringBuffer sql = new StringBuffer();
	private List<Object> parameters = new ArrayList<Object>();
	
	private FiledBuilder filedBuilder;
	private WhereBuilder whereBuilder;
	private GroupBuilder groupBuilder;
	private HavingBuilder havingBuilder;
	private OrderBuilder orderBuilder;
	private PageBuilder pageBuilder;
	
	private boolean hasGroup = false;
	private String table;
	
	public SQLBuilder(String table) {
		this.table = table;
	}
	
	public boolean hasTable(){
		return table != null && !"".equals(table);
	}
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	
	public void setFiledBuilder(FiledBuilder filedBuilder) {
		this.filedBuilder = filedBuilder;
	}

	public void setWhereBuilder(WhereBuilder whereBuilder) {
		this.whereBuilder = whereBuilder;
	}

	public void setGroupBuilder(GroupBuilder groupBuilder) {
		this.groupBuilder = groupBuilder;
		hasGroup = true;
	}

	public void setHavingBuilder(HavingBuilder havingBuilder) {
		this.havingBuilder = havingBuilder;
	}

	public void setOrderBuilder(OrderBuilder orderBuilder) {
		this.orderBuilder = orderBuilder;
	}

	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}

	public String createSelectSQL(){
		
		sql.delete(0, sql.length());
		parameters.clear();
		
		sql.append("select ");
		if(filedBuilder != null){
			filedBuilder.resetSQL(this);
		}else{
			sql.append("*");
		}
		
		sql.append(" from ").append(table);
		
		// 组装where
		if(whereBuilder != null){
			sql.append(" where ");
			whereBuilder.resetSQL(this);
		}
		// 组装group
		if(groupBuilder != null){
			sql.append(" group by ");
			groupBuilder.resetSQL(this);
		}
		// 组装having
		if(havingBuilder != null){
			sql.append(" having  ");
			havingBuilder.resetSQL(this);
		}
		// 组装order
		if(orderBuilder != null){
			sql.append(" order by  ");
			orderBuilder.resetSQL(this);
		}
		// 组装分页
		if(pageBuilder != null){
			sql.append(" limit  ");
			pageBuilder.resetSQL(this);
		}
		String s = sql.toString();
		System.out.println(s);
		return s;
	}
	public String createInsertSQL(Map<String, Object> map){
		sql.delete(0, sql.length());
		parameters.clear();
		
		sql.append("insert into ")
		.append(table);
		StringBuilder fields = new StringBuilder(" (");
		StringBuilder values = new StringBuilder("");
		
		for(Map.Entry<String, Object> entry :map.entrySet()){
			fields.append(entry.getKey()).append(",");
			parameters.add(entry.getValue());
			values.append("?").append(",");
		}
		sql.append(fields.substring(0,fields.length()-1));
		sql.append(") values (");
		sql.append(values.substring(0,values.length()-1));
		sql.append(")");
		String s = sql.toString();
		return s;
	}
	public String createUpdateSQL(Object entity){
		Map<String, Object> map = EntityUtil.getEntityInfoWithoutNULL(entity);
		sql.delete(0, sql.length());
		parameters.clear();
		
		sql.append("update ").append(table)
		.append(" set ");
		for(Map.Entry<String, Object> entry : map.entrySet()){
			sql.append(entry.getKey()).append(" = ? ,");
			parameters.add(entry.getValue());
		}
		sql.deleteCharAt(sql.length()-1);
		
		if(whereBuilder != null){
			sql.append(" where ");
			whereBuilder.resetSQL(this);
		}
		String s = sql.toString();
		System.out.println(s);
		return s;
	}
	
	public String createDeleteSQL(){
		sql.delete(0, sql.length());
		parameters.clear();
		
		sql.append(" delete from ").append(table);
		if(whereBuilder != null){
			sql.append(" where ");
			whereBuilder.resetSQL(this);
		}
		String s = sql.toString();
		System.out.println(s);
		return s;
	}
	public StringBuffer getSql() {
		return sql;
	}

	public List<Object> getParameters() {
		return parameters;
	}


	public boolean isHasGroup() {
		return hasGroup;
	}

	public SQLBuilder addFiled(String... fileds){
		if(filedBuilder == null){
			filedBuilder = new FiledBuilder(this);
		}
		filedBuilder.addFiled(fileds);
		return this;
	}
	
	public SQLBuilder addCount(){
		return addCount(null);
	}
	
	public SQLBuilder addCount(String countAlias){
		if(filedBuilder == null){
			filedBuilder = new FiledBuilder(this);
		}
		filedBuilder.addCount(countAlias);
		return this;
	}
	
	public SQLBuilder addSum(){
		return addSum(null);
	}
	
	public SQLBuilder addSum(String sumAlias){
		if(filedBuilder == null){
			filedBuilder = new FiledBuilder(this);
		}
		filedBuilder.addSum(sumAlias);
		return this;
	}
	
	private void initWhere() {
		if(whereBuilder == null){
			whereBuilder = new WhereBuilder(this);
		}
	}

	public SQLBuilder addWhereEQ(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.EQ, value));
		return this;
	}


	public SQLBuilder addWhereNE(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.NEQ, value));
		return this;
	}
	public SQLBuilder addWhereGT(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.GT, value));
		return this;
	}
	public SQLBuilder addWhereGE(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.GE, value));
		return this;
	}
	
	public SQLBuilder addWhereLE(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.LE, value));
		return this;
	}
	
	public SQLBuilder addWhereLT(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.LT, value));
		return this;
	}
	
	public SQLBuilder addWhereLIKE(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.LIKE, value));
		return this;
	}
	public SQLBuilder addWhereIN(String key,Object... values){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.IN, values));
		return this;
	}
	
	public SQLBuilder addWhereIN(String key,List<?> value){
		return addWhereIN(key, value.toArray());
	}
	
	
	public SQLBuilder addWhereIN(String key,Object value){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.IN, value));
		return this;
	}
	
	public SQLBuilder addWhereNOTIN(String key,Object... values){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.NOTIN, values));
		return this;
	}
	public SQLBuilder addWhereNOTIN(String key,List<?> value){
		return addWhereNOTIN(key, value.toArray());
	}
	
	public SQLBuilder addWhereBETWEEN(String key,Object start ,Object end){
		initWhere();
		whereBuilder.add(new QueryCondition(key, Type.BETWEEN, new Object[]{start,end}));
		return this;
	}
	
	public SQLBuilder addGroup(String... fileds){
		if(groupBuilder == null){
			groupBuilder = new GroupBuilder(this);
		}
		for(String column :fileds){
			groupBuilder.selectColumn(column);
		}
		return this;
	}
	
	public SQLBuilder addOrderASC(String fileds){
		if(orderBuilder == null){
			orderBuilder = new OrderBuilder(this);
		}
		orderBuilder.addASC(fileds);
		return this;
	}
	public SQLBuilder addOrderDESC(String fileds){
		if(orderBuilder == null){
			orderBuilder = new OrderBuilder(this);
		}
		orderBuilder.addDESC(fileds);
		return this;
	}
	
	public SQLBuilder addPqge(int start ,int size){
		if(pageBuilder == null){
			pageBuilder = new PageBuilder(this);
		}
		pageBuilder.setStartIndex(start);
		pageBuilder.setPageSize(size);
		return this;
	}
	
	public SQLBuilder addPqge(int size){
		if(pageBuilder == null){
			pageBuilder = new PageBuilder(this);
		}
		pageBuilder.setStartIndex(0);
		pageBuilder.setPageSize(size);
		return this;
	}
}

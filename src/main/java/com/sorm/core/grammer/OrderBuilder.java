package com.sorm.core.grammer;

import java.util.ArrayList;
import java.util.List;


public class OrderBuilder extends GrammarBuilder {
	
	public OrderBuilder(SQLBuilder sqlBuilder) {
		super(sqlBuilder);
		sqlBuilder.setOrderBuilder(this);
	}

	private final List<Order> orders = new ArrayList<Order>();
	
	private class Order {
		
		private final String column;
		private final String mode;
		
		public Order(String column, String mode) {
			this.column = column;
			this.mode = mode;
		}
		
		public String getColumn() {
			return column;
		}

		public String getMode() {
			return mode;
		}
		
	}
	
	/**
	 * 添加排序条件，具体字段先后顺序和调用的顺序一致
	 * @param column
	 * @param mode
	 * @return
	 */
	public OrderBuilder add(String column, String mode) {
		orders.add(new Order(column, mode));
		return this;
	}
	
	/**
	 * 添加升序排序条件
	 *
	 * @param column
	 * @return
	 */
	public OrderBuilder addASC(String column) {
		orders.add(new Order(column, "asc"));
		return this;
	}
	
	/**
	 * 添加兼续排序条件
	 *
	 * @param column
	 * @return
	 */
	public OrderBuilder addDESC(String column) {
		orders.add(new Order(column, "desc"));
		return this;
	}
	
	@Override
	public void resetSQL(SQLBuilder sqlBuilder) {
		StringBuffer sql = sqlBuilder.getSql();
		
		int size = orders.size();
		int index = 0;
		if (size > 0) {
			for (Order order : orders) {
				if(order.getMode().equals("asc")){
					sql.append(order.getColumn())
					.append("  ").append("asc");
				}else{
					sql.append(order.getColumn())
					.append("  ").append("desc");
				}
				if (index < (size - 1)) {
					sql.append(",");
				}
				index++;
			}
		}
	}
}

package com.sorm.core;

import com.sorm.core.annotation.Table;
import com.sorm.core.annotation.TablePK;

@Table(value="dept")
public class Dept {
	
	@TablePK
	private int id;
	
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Dept [id=" + id + ", name=" + name + "]";
	}
	
}

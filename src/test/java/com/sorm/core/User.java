package com.sorm.core;

import java.util.Date;

import com.sorm.core.annotation.Cascade;
import com.sorm.core.annotation.Table;
import com.sorm.core.annotation.TablePK;

@Table(value="user")
public class User {
	
	@TablePK
	private long id;
	
	private String name;
	private Date birthday;
	private long deptId;
	
	
	@Cascade(value="deptId")
	private Dept dept;
	
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", birthday=" + birthday
				+ ", deptId=" + deptId + ", dept=" + dept + "]";
	}
	public long getDeptId() {
		return deptId;
	}
	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}

	
	
}

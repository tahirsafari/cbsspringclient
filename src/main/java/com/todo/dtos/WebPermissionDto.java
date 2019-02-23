package com.todo.dtos;

public class WebPermissionDto {
	private int userId;
	private String name;
	private String desc;
	
	
	public WebPermissionDto(int userId, String name, String desc) {
		super();
		this.userId = userId;
		this.name = name;
		this.desc = desc;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}

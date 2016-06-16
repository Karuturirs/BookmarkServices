package com.bookmark.restservice.dao;

public class Folder {
	int id;
	String foldername;
	String type;
	String parenturl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFoldername() {
		return foldername;
	}
	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParenturl() {
		return parenturl;
	}
	public void setParenturl(String parenturl) {
		this.parenturl = parenturl;
	}


}

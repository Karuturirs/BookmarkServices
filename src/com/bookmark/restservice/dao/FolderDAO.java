package com.bookmark.restservice.dao;

public interface FolderDAO {
	
	public void createFolder();
	public void updateFolder();
	public void getFolderByName();
	public void getFolderByNameAndPath();
	public void getFolderById();
	public void moveFolder();
	public void deleteFolder();

}

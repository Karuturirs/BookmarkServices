package com.bookmark.restservice.dao;

public interface LinkDAO {
	public void createLink();
	public void updateLink();
	public void getLinkByName();
	public void getLinkByNameAndPath();
	public void getLinkById();
	public void moveLink();
	public void deleteLink();
}

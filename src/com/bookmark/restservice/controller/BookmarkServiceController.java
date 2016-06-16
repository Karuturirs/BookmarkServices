package com.bookmark.restservice.controller;

import java.io.IOException;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookmark.restservice.model.BookMark;
import com.bookmark.restservice.model.EditInput;
import com.bookmark.restservice.model.Input;
import com.bookmark.restservice.mysql.service.GenericService;
import com.bookmark.restservice.utils.BookMarkmaster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class BookmarkServiceController {
	@Autowired
	GenericService bookMarkService;
	private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson;
	@RequestMapping(value = "/new", method = RequestMethod.POST,headers = "Content-type=application/json")
	public @ResponseBody JSONObject getRequest(@RequestBody JSONObject jsonObject)
	{
		gson=gsonBuilder.create();
		try {
			Input input=gson.fromJson(jsonObject.toJSONString(), Input.class);
			if(input!=null){
				jsonObject=insertNewrecord(input);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return jsonObject;
	}
	//Method to edit bookmark
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST,headers = "Content-type=application/json")
	public @ResponseBody JSONObject edit(@RequestBody JSONObject jsonObject)
	{
		gson=gsonBuilder.create();
		try {
			EditInput editInput=gson.fromJson(jsonObject.toJSONString(), EditInput.class);
			jsonObject=updateRecord(editInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	//Method to move path
	@RequestMapping(value = "/move", method = RequestMethod.POST,headers = "Content-type=application/json")
	public @ResponseBody JSONObject move(@RequestBody JSONObject jsonObject)
	{
		gson=gsonBuilder.create();
		try {
			EditInput editInput=gson.fromJson(jsonObject.toJSONString(), EditInput.class);
			jsonObject=movePath(editInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	//Method to delete
	@RequestMapping(value = "/delete", method = RequestMethod.POST,headers = "Content-type=application/json")
	public @ResponseBody JSONObject delete(@RequestBody JSONObject jsonObject)
	{
		gson=gsonBuilder.create();
		try {
			EditInput editInput=gson.fromJson(jsonObject.toJSONString(), EditInput.class);
			jsonObject=deleteRecord(editInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return jsonObject;
	}
	private JSONObject insertNewrecord(Input input) throws JsonParseException, JsonMappingException, JAXBException, IOException, ParseException
	{
		BookMark bookmark=new BookMark();
		bookmark.setName(input.getName()!=null?input.getName():"");
		bookmark.setDescription(input.getDescription()!=null?input.getDescription():"");
		bookmark.setType(input.getType()!=null?input.getType():"");
		bookmark.setUrl(input.getUrl()!=null?input.getUrl():"");
		bookmark.setPath(input.getNewpath()!=null?input.getNewpath():"");
		bookMarkService.save(bookmark);
		return getAll();
	}
	private JSONObject updateRecord(EditInput editInput) throws JsonParseException, JsonMappingException, JAXBException, IOException, ParseException
	{
		Collection<BookMark> bookmarkList=	bookMarkService.findByHSQLQuery("from bookmark_favourites where path LIKE '"+editInput.getOldpath()+"' ");
		for(BookMark bookmark1:bookmarkList)
		{
			String path=bookmark1.getPath();
			if(path.equals(editInput.getOldpath())){
				bookmark1.setName(editInput.getName());
			}
			path.replace(editInput.getOldpath(), editInput.getNewpath());
			bookmark1.setPath(path);
			bookMarkService.update(bookmark1);
		}
		return getAll();
	}
	private JSONObject movePath(EditInput editInput) throws JsonParseException, JsonMappingException, JAXBException, IOException, ParseException
	{
		Collection<BookMark> bookmarkList=	bookMarkService.findByHSQLQuery("from bookmark_favourites where path LIKE '"+editInput.getOldpath()+"' ");
		for(BookMark bookmark1:bookmarkList)
		{
			String path=bookmark1.getPath();
			if(path.equals(editInput.getOldpath())){
				bookmark1.setName(editInput.getName());
			}
			path.replace(editInput.getOldpath(), editInput.getNewpath());
			bookmark1.setPath(path);
			bookMarkService.update(bookmark1);
		}
		return getAll();
	}
	private JSONObject deleteRecord(EditInput editInput) throws JsonParseException, JsonMappingException, JAXBException, IOException, ParseException
	{
		Collection<BookMark> bookmarkList=	bookMarkService.findByHSQLQuery("from bookmark_favourites where path LIKE '"+editInput.getOldpath()+"' ");
		for(BookMark bookmark1:bookmarkList )
		{
			bookMarkService.delete(bookmark1);
		}
		return getAll();
	}
	private JSONObject getAll() throws JsonParseException, JsonMappingException, JAXBException, IOException, ParseException{
		Collection<BookMark> bookmarkList=bookMarkService.getAll();
		BookMarkmaster bookMarkmaster=new BookMarkmaster();
		bookMarkmaster.setDataList(bookmarkList);
		JSONObject outputJson=pojo2Json(bookMarkmaster);
		return outputJson;
	}

	public JSONObject pojo2Json(Object obj) throws JAXBException,
			JsonParseException, JsonMappingException, IOException,
			ParseException {
		JSONObject object = new JSONObject();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(obj);
		JSONParser jsonParser = new JSONParser();
		object = (JSONObject) jsonParser.parse(jsonString);
		return object;
	}
}

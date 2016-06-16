package com.bookmark.restservice.test;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONObject;

import com.bookmark.restservice.utils.ExternalWebserviceCall;

public class Test 
{
  public static void main(String[] args) throws MalformedURLException, IOException{
	  ExternalWebserviceCall extserv = new ExternalWebserviceCall("http://localhost:8080/BookmarkService");
	  JSONObject jsonobj =new JSONObject();
	  jsonobj.put("name", "helxvlo");
	  extserv.httpPostMethod(jsonobj, "bookmark");
  }
}

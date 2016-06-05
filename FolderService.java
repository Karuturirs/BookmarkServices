package com.bookmark.restservices.controller;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.bookmark.restservices.model.BookMark;
import com.bookmark.restservices.model.Input;
import com.bookmark.restservices.model.Output;
import com.bookmark.restservices.utils.BookMarkmaster;
import com.bookmark.restservices.utils.JdbcUtil;


@Path("/rest")
public class FolderService {
	
	@GET
	@Path("/start")
	@Produces("text/plain")
	public String start()  {
		
		return "Hello World";
	}
	
	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public JSONObject responseMsgForNew(Input input)  {
		Output newoutput = new Output(); 
		Response res = ValidateInput(input,newoutput,"new");
		if(res.getStatus()==200){
			 insertNewRecord(input,newoutput);
		}
		return pojo2Json(newoutput);

	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public JSONObject responseMsgForUpdate(Input input) {
		Output updateoutput = new Output(); 
		Response res = ValidateInput(input,updateoutput,"edit");
		if(res.getStatus()==200){
		   updateRecord(input,updateoutput);
		}
		return pojo2Json(updateoutput);

	}
	
	@POST
	@Path("/move")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public JSONObject responseMsgForMove(Input input) {
		Output moveoutput = new Output(); 
		Response res = ValidateInput(input,moveoutput,"move");
		if(res.getStatus()==200){
			updateRecord(input,moveoutput);
		}
		return pojo2Json(moveoutput);
	}
	
	
	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public JSONObject responseMsgForRemove(Input input) {
		
		Output removeoutput = new Output(); 
		Response res = ValidateInput(input,removeoutput,"remove");
		if(res.getStatus()==200){
			removeRecord(input,removeoutput);
		}
		return pojo2Json(removeoutput);
	}
	
	
	@GET
	@Path("/getall")
	@Produces("application/json")
	public JSONObject getAll() throws FileNotFoundException, IOException, SQLException, JAXBException, ParseException{
		HashSet<BookMark> collectionbookmark = new HashSet<BookMark>();
		BookMarkmaster bookMarkmaster=new BookMarkmaster();
		Connection conn=JdbcUtil.getInstance().getMySqlConnection();
		String sql ="Select id,name,type,path,url,description from "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename");
		PreparedStatement prest = conn.prepareStatement(sql); 
		ResultSet rs = prest.executeQuery(sql);
		 while(rs.next()){
			 BookMark bookmark = new BookMark();
			 bookmark.setId(rs.getInt(1));
			 bookmark.setName(rs.getString(2));
			 bookmark.setType(rs.getString(3));
			 bookmark.setPath(rs.getString(4));
			 bookmark.setUrl(rs.getString(5));
			 bookmark.setDescription(rs.getString(6));
			 collectionbookmark.add(bookmark);	 
		 }
		bookMarkmaster.setDataList(collectionbookmark);
		JSONObject outputJson=pojo2Json(bookMarkmaster);
		return outputJson;
	}
	
	
	
	public JSONObject pojo2Json(Object obj){
		JSONObject object = new JSONObject();
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(obj);
			JSONParser jsonParser = new JSONParser();
			object = (JSONObject) jsonParser.parse(jsonString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return object;
	}
	
	public void removeRecord(Input input, Output output) {
		try{
		Connection conn=JdbcUtil.getInstance().getMySqlConnection();
		String deletesql = "DELETE FROM "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename")+" where  path= '"+input.getNewpath()+"' OR path LIKE '"+input.getNewpath()+"/%'";
		System.out.println(deletesql);
		PreparedStatement prest = conn.prepareStatement(deletesql); 
		 prest.execute(); 
		 output.setStatus(200);
		 output.setMessage("OK");
		 output.setDescription("Successfully Updated FOLDER record to DB.");
		}catch(Exception e){
			output.setStatus(800);
			output.setMessage("Unable to delete record from DB.");
			output.setDescription(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void updateRecord(Input input, Output output)  {
		try{
			if(input.getType().equals("FOLDER")){
				HashSet<Input> bookmarkList = FetchRecuiredRecords(input,output);
				String updatemainsql = "UPDATE "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename")+" SET name =?, path= ? where path = ?";
				String updatesql = "UPDATE "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename")+" SET path= ? where path = ?"; 
				Connection conn=JdbcUtil.getInstance().getMySqlConnection();
				PreparedStatement prest =null;
				for(Input bookmark:bookmarkList)
				{
					if(bookmark.getOperation().equals("main")){
						prest = (PreparedStatement) conn.prepareStatement(updatemainsql); 
						prest.setString(1, bookmark.getName());
						prest.setString(2, bookmark.getNewpath());
						prest.setString(3, bookmark.getOldpath());
						System.out.println(updatemainsql+ " ->"+bookmark.getName()+" "+bookmark.getNewpath()+" "+bookmark.getOldpath());
						prest.execute();
					}else{
						prest = (PreparedStatement) conn.prepareStatement(updatesql);  
						prest.setString(1, bookmark.getNewpath());
						prest.setString(2, bookmark.getOldpath());
						System.out.println(updatesql+" ->"+bookmark.getNewpath()+" "+bookmark.getOldpath());
						prest.execute();
					}
					
				}
				//int[] success = prest.executeBatch();
				//System.out.println("Updated "+success.length+" records.");
				//return  Response.status(200).entity("Successfully updated "+success.length+" records").build();
				//return  Response.status(200).entity("Successfully updated records").build();
				output.setStatus(200);
				output.setMessage("OK");
				output.setDescription("Successfully Updated FOLDER record to DB.");
			}else{
				FetchRecuiredRecords(input,output);
				output.setStatus(200);
				output.setMessage("OK");
				output.setDescription("Successfully updated LINK record to DB.");
			}
		}catch(Exception e){
			output.setStatus(800);
			output.setMessage("Unable to Update record into DB.");
			output.setDescription(e.getMessage());
			e.printStackTrace();
		}
	}

	public Response ValidateInput(Input input,Output output, String function) {
			
		String type = input.getType();
		if(type == null || type.equals("")){
			output.setStatus(401);
			output.setMessage("Invalid input");
			output.setDescription("type attribute value should be a FOLDER/LINK, Cannot be blank");
			return Response.status(401).entity("type attribute value should be a FOLDER/LINK, Cannot be blank").build();
		}
		if( type.equalsIgnoreCase("FOLDER") || type.equalsIgnoreCase("LINK") ){
			
		}else{
			output.setStatus(401);
			output.setMessage("Invalid input");
			output.setDescription("type attribute value should either FOLDER/LINK");
			return Response.status(401).entity("type attribute value should either FOLDER/LINK").build();
		}
		String name = input.getName();
		if(name == null || name.equals("")){
			output.setStatus(401);
			output.setMessage("Invalid input");
			output.setDescription("name attribute value cannot be blank or null");
			return Response.status(401).entity("name attribute value cannot be blank or null").build();
		}
		String path = input.getNewpath();
		if(path == null || path.equals("")){	
			output.setStatus(401);
			output.setMessage("Invalid input");
			output.setDescription("newpath attribute value cannot be blank or null");
			return Response.status(401).entity("newpath attribute value cannot be blank or null").build();
		}
		if(type.equalsIgnoreCase("LINK")){
			String url = input.getUrl();
			if(url == null || url.equals("")){
				output.setStatus(401);
				output.setMessage("Invalid input");
				output.setDescription("url attribute value cannot be blank or null");
				return Response.status(401).entity("url attribute value cannot be blank or null").build();
			}
		}
		
		if(function.equals("edit")){
			String oldpath = input.getOldpath();
			if(oldpath == null || oldpath.equals("")){
				output.setStatus(401);
				output.setMessage("Invalid input");
				output.setDescription("oldpath attribute value cannot be blank or null");
				return Response.status(401).entity("oldpath attribute value cannot be blank or null").build();
			}
		}
		output.setStatus(200);
		output.setMessage("OK");
		output.setDescription("Successfully Validated");
		return Response.status(200).entity("Successfully Validated").build();
		
	}

	public void insertNewRecord(Input input, Output output)  {
		// TODO Auto-generated method stub
	
		try{ 
			Connection conn=JdbcUtil.getInstance().getMySqlConnection();
		
			if(input.getType().equalsIgnoreCase("FOLDER")){
				String sql = "INSERT INTO "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename")+" (name , type , path) VALUES (?,?,?)";
				 PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql);  
				  prest.setString(1, input.getName()); 
				  prest.setString(2, input.getType());
				  prest.setString(3, input.getNewpath()); 
				  prest.execute(); 
			}else{
				String sql = "INSERT INTO "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename")+" (name , type , path, url , description) VALUES (?,?,?,?,?)";
				 PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql);  
				 prest.setString(1, input.getName()); 
				  prest.setString(2, input.getType());
				  prest.setString(3, input.getNewpath());
				  prest.setString(4, input.getUrl());
				  prest.setString(5, input.getDescription());
				  prest.execute(); 
			}
			output.setStatus(200);
			output.setMessage("OK");
			output.setDescription("Successfully inserted new record to DB.");
		}catch(Exception e){
			output.setStatus(800);
			output.setMessage("Unable to insert into new record to DB.");
			output.setDescription(e.getMessage());
			e.printStackTrace();
			//return Response.status(401).entity("UnSuccessfully insertion").build();
		}finally{
			
		}
		//return Response.status(200).entity("Successfully inserted").build();
	}
	
	public HashSet<Input> FetchRecuiredRecords(Input input, Output output) throws SQLException  {
		// TODO Auto-generated method stub
	
		HashSet<Input> inputcollect = new HashSet<Input>(); 
		try{
			Connection conn=JdbcUtil.getInstance().getMySqlConnection();
			System.out.println(conn);
			if(input.getType().equalsIgnoreCase("FOLDER")){
				String sql = "select * from "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename")+" where path = '"+input.getOldpath()+"' or path LIKE '"+input.getOldpath()+"/%'";
				
				 PreparedStatement prest = conn.prepareStatement(sql);
				 ResultSet rs = prest.executeQuery(sql);
				
				 while(rs.next()){
					  Input ip = new Input();
					  
					  String inputoldpath =input.getOldpath();
					  String recname = rs.getString("path");
					  if(inputoldpath.equals(recname) && input.getType().equalsIgnoreCase("FOLDER")){
						  ip.setOperation("main");
						  ip.setOldname(rs.getString("name"));
						  ip.setName(input.getName());
						  ip.setOldpath(recname);
						  ip.setNewpath(input.getNewpath());
					  }else{
						  ip.setOperation("child");
						  ip.setName(rs.getString("name"));
						  ip.setOldpath(recname);
						  ip.setNewpath(recname.replace(inputoldpath,input.getNewpath()));
						  System.out.println(recname+"   "+inputoldpath+" "+input.getNewpath());
					  }
					  inputcollect.add(ip);
				 }
			}else{
				 String sql = "UPDATE "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename")+" SET name =?, path=?, url=?, description=? where path = '"+input.getOldpath()+"'";
				
				 PreparedStatement prest = conn.prepareStatement(sql); 
				  prest.setString(1, input.getName()); 
				  prest.setString(2, input.getNewpath());
				  prest.setString(3, input.getUrl()); 
				  prest.setString(4, input.getDescription()); 
				  prest.execute(); 
				  output.setStatus(200);
				  output.setMessage("OK");
				  output.setDescription("Successfully updated link record in DB.");
			}
		}catch(Exception e){
			output.setStatus(800);
			output.setMessage("Unable to fetch/update existing records in DB.");
			output.setDescription(e.getMessage());
			e.printStackTrace();
		}
		return inputcollect;
	}
	
	/*
	@POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) throws IOException {
          
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
 
        // Get file data to save
        List<InputPart> inputParts = uploadForm.get("attachment");
 
        for (InputPart inputPart : inputParts) {
            try {
 
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
   
                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,
                        null);
 
                byte[] bytes = IOUtils.toByteArray(inputStream);
                // constructs upload file path
                fileName = "D:/" + fileName;
                writeFile(bytes, fileName);
 
                  
                return Response.status(200).entity("Uploaded file name : " + fileName)
                        .build();
 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
 
    private String getFileName(MultivaluedMap<String, String> header) {
 
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
 
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
 
                String[] name = filename.split("=");
 
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }
 
    // Utility method
    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("not exist> " + file.getAbsolutePath());
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
    }*/
	/*
	@GET
	@Path("/downloadCSV")
	@Produces("text")
	public String downloadData() throws IOException, SQLException {
	    String filename = "test.csv";
	 //   response.setContentType("text/csv");
	 //   response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
	    FileWriter fw = new FileWriter(filename);
	    Connection conn=JdbcUtil.getInstance().getMySqlConnection();
		String sql ="Select * from "+JdbcUtil.getInstance().getProperty("mysql.favourite.tablename");
		PreparedStatement prest = conn.prepareStatement(sql); 
		ResultSet rs = prest.executeQuery(sql);
		 while(rs.next()){
			 System.out.println(rs.getInt(1));
             fw.append(String.valueOf(rs.getInt(1)));
             fw.append(',');
             fw.append(rs.getString(2));
             fw.append(',');
             fw.append(rs.getString(3));
             fw.append(',');
             fw.append(rs.getString(4));
             fw.append(',');
             fw.append(rs.getString(5));
             fw.append(',');
             fw.append(rs.getString(6));
             fw.append(',');
             fw.append(rs.getString(7));
             fw.append('\n');
		 }
		 fw.flush();
         fw.close();
         return "";
	}
	*/
	
}

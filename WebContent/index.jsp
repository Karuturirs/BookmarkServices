<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Bookmark Service</title>
</head>
<body>
<center>	<h4>BookmarkService APPLICATION.</h4> </center>
	
	 <form action="http://localhost:8080/Bookmark/upload/file" method="post" enctype="multipart/form-data">
 		<center>
	   <p>
		Select a file : <input type="file" name="file" size="45" />
	   </p>
 
	   <input type="submit" value="Upload It" />
	   </center>
	</form>
	<br/>
	<br/>
	<a href="http://localhost:8080/Bookmark/download/file">Import data from DB.</a>
</body>
</html>
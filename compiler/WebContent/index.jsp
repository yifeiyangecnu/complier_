<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>源文件上传</h1>
<form method="post" action="UploadServlet" enctype="multipart/form-data">
	选择源文件:
	<input type="file" name="uploadFile" />
	<br/><br/>
	<input type="submit" value="上传" />
</form>
<br>
源文本输入:
<form method="post" action="TextSourceServlet"  >
	<textarea rows="30" cols="100"   name="source"  ></textarea>
	<input type="submit" value="源文本上传" />
</form>
</body>
</html>
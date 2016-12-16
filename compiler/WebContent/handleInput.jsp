<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
String name = request.getParameter("name");
out.println(name);
%>
<input type="text"  name="name" value=<%= name %>></input>
<%if(name.equals("name")) 
{%>
<span  style="color: green"><%= name %></span>
<%} else{
%>
<span  style="color: red"><%= name %></span>
<%} %>
</body>
</html>
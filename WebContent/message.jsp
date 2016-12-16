<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
  import="java.util.List"    
  import="compiler.lex.domain.Output"
  import="compiler.lex.domain.TokenColorType"
  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件上传结果</title>
</head>
<body>
    <%List<String> tokens=(List<String>)request.getAttribute("tokens") ;
    List<Output> outputs=(List<Output>)request.getAttribute("outputs");
%>
    <center>
    <%     for(int i=0;i<tokens.size();++i){
    %>
        <h2><%=tokens.get(i) %></h2>
        <%} %>
    </center>

    <%
    int i=0;
    for(Output output:outputs)
    {%>
    <%if(i!=output.getLineNumber()) {%>
    <br>
    <span><%= output.getLineNumber()+"\t" %></span><%} %>
    <span  style=<%="\"color:"+output.getTokenColor().getColorStr()+"\""  %>><%= output.getTokenName() %></span>
<%   
	i=output.getLineNumber();
    }%> 
</body>
</html>
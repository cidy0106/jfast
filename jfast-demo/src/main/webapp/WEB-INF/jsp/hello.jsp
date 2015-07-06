<%@page import="com.xidige.jfast.web.RequestContext"%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jsp～～</title>
</head>
<body>
<form action="sayHtml" method="post">
<input type="text" name="username" value="" />
<input type="submit" value="提交"  />
</form>
hi,#<%
out.print(request.getAttribute("reply"));
%>#
</body>
</html>
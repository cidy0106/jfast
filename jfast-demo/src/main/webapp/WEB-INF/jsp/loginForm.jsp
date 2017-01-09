<%@page import="com.xidige.jfast.web.RequestContext"%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
<form action="login" method="post">
<input type="text" name="username" value="" />
<input type="password" name="password" value="" />
<input type="submit" value="提交"  />
</form>

</body>
</html>
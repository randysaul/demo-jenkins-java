<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello!!</title>
</head>
<body>
	<h1>Well Done!!</h1>
	<hr>
	<c:forEach begin="1" end="10" var="i">
		${i} <br>
	</c:forEach>
</body>
</html>
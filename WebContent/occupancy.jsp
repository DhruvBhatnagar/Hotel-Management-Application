<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="comp9321.dao.*, java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style.css" media="screen" />
<title>Hotel Management</title>
</head>
<body>
	Occupancy
	<table>
		<tr>
			<th>Hotel Branch</th>
			<th>Location</th>
			<th>Number of occupied rooms</th>
			<th>Number of available rooms</th>

		</tr>
		<c:forEach items="${occupancy}" var="h">
			<tr>
				<td>${h.name}</td>
				<td>${h.city}</td>
				<td>${h.occupied}</td>
				<td>${h.available}</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>
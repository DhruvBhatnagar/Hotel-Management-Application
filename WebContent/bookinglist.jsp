<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="comp9321.dao.*, java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style.css" media="screen" />
<title>Manage Owner</title>
</head>
<body>
<jsp:include page="/logout.jsp"/>
	<table>
		<tr>
			<th>Booking Reference</th>
		</tr>
		<c:forEach items="${Booking}" var="b">
			<tr>
				<td>${b.id}</td>
				<td>
					<form action=staff method =  "GET">
						<input type=hidden name='selectedbookingid' value="${b.id}">
						<input type=hidden name='action' value="selectroomforbooking">
						<input type="submit" class="editbutton" value="Edit" >
					</form>
				</td>

			</tr>
		</c:forEach>
	</table>
	
	List of rooms occupied:
	<table>
		<tr>
			<th>Room ID</th>
			<th>Class</th>
			<th>City</th>
			<th>Booking ID</th>
			<c:forEach items="${OccupiedRooms}" var="b">
				<tr>
					<td>${b.id}</td>
					<td>${b.roomTypeName}</td>
					<td>${b.hotelBranch}</td>
					<td>${b.bkid}</td>
				</tr>
			</c:forEach>

		</tr>
	</table>
	
</body>
</html>
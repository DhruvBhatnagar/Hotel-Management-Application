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
<jsp:include page="/logout.jsp"/>
	Create a discount
	<form action=owner method="post">
		<br>Class:
		<select name="class">
			<c:forEach items="${allRoomTypes}" var="r">
				<option value="${r.id}">${r.tname}</option>
			</c:forEach>
		</select>
		
		<br>Hotel:
		<select name="hotel">
			<c:forEach items="${allHotels}" var="h">
				<option value="${h.id}">${h.city}</option>
			</c:forEach>
		</select>
				
	 	<br>Rate: <input type="text" name="amt">
	 			<c:if test="${invalidnumber eq false}">
			<font color="red">*invalid number</font>
		</c:if>
		<br><br>From 	<select name="fd">
			<c:forEach begin="01" end="31" varStatus="loop">
				<option value="${loop.index}">${loop.index}</option>
			</c:forEach>			
		</select>

		<select name="fm">
			<c:forEach begin="01" end="12" varStatus="loop">
				<option value="${loop.index}">${loop.index}</option>
			</c:forEach>			
		</select>
				<select name="fy">
			<c:forEach begin="2014" end="2020" varStatus="loop">
				<option value="${loop.index}">${loop.index}</option>
			</c:forEach>			
		</select>
		<c:if test="${invaliddate eq false}">
			<font color="red">*invalid date</font>
		</c:if>
		<br><br>
		To    <select name="td">
			<c:forEach begin="01" end="31" varStatus="loop">
				<option value="${loop.index}">${loop.index}</option>
			</c:forEach>			
		</select>
		
		<select name="tm">
			<c:forEach begin="01" end="12" varStatus="loop">
				<option value="${loop.index}">${loop.index}</option>
			</c:forEach>			
		</select>
				<select name="ty">
			<c:forEach begin="2014" end="2020" varStatus="loop">
				<option value="${loop.index}">${loop.index}</option>
			</c:forEach>			
		</select>
		<br><br>
		<input type='hidden' name="action" value="submitdiscount">
		<input type="submit" name="discountbutton" value="Add Discount">
		
	</form>

</body>
</html>
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
	Confirm Discount<br>
	Class: ${discountdetail.tname}<br>
	Discount: -${discountdetail.rate}<c:if test="${discountdetail.discounttype eq 'percentage'}">%</c:if><br>
	From ${discountdetail.sfrom} To ${discountdetail.sto}<br>
	Date span: ${discountdetail.datediff}<br>
	<form action=owner method="post">
		<input type='hidden' name='action' value='confirmdiscount'>
		<input type='submit' value='Confirm Discount'>
	</form>
	<form action=owner method="post">
		<input type='submit' value='Cancel Discount'>
		<input type='hidden' name='action' value='canceldiscount'>
	</form>
</body>
</html>
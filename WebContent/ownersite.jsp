<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style.css" media="screen" />
<title>Hotel Management</title>
</head>
<jsp:include page="/logout.jsp"/>
<body>
	<p>Owner's Page</p>
	<form action=owner method="get">
		<input type='hidden' name="action" value="viewoccupancy"> <input
			type="submit" class='editbutton' value="View Occupancy">

	</form>
	<form action=owner method="get">
		<input type='hidden' name="action" value="editdiscount"> <input
			type="submit" class='editbutton' value="Edit Discounts">
	</form>
</body>
</html>
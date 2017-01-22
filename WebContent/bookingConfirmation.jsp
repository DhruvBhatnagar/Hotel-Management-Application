<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="zc" uri="http://java.sun.com/jsp/jstl/core" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Booking Confirmation</title>
</head>
<body>
<zc:choose>
	<zc:when test="${error == true }">
		<p>
			Your Booking was unable to be processed.
			 <div style="color:blue">
			 	${error_messages }
			 </div>
		</p>
	</zc:when>
	<zc:when test="${error == false}">
		<div style="color:black">
			Your Booking was successfully Processed.
			<br>
			Your Booking Number is: ${bookNumb }
			<br>
			Your Unique PIN is: ${pin}
			<br>
			Your URI for accessing Booking Management is: <a href="http://localhost:8080/Assign2Merge/custLogin?id=${bookNumb }">http://localhost:8080/Assign2Merge/custLogin?id=${bookNumb }</a>
		</div>
	</zc:when>
</zc:choose>

</body>
</html>
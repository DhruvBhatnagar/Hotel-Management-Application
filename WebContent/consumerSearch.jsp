<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="zc" uri="http://java.sun.com/jsp/jstl/core" %>    
<jsp:useBean id="htlName" class="comp9321.bean.HotelsDTO" scope="session" />
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css">

 <script>
 $(function() {
	    $( "#checkInDate" ).datepicker();
	    $( "#checkOutDate" ).datepicker();

	  });
</script>

<title>Customer Booking</title>
</head>
<body>
	<h1> Make a Booking here</h1>
	<div  style="color:blue">
		${errors }
	</div>
	<form action="ControlServlet" method="post">
		Checkin Date: <input type="text" id="checkInDate" name="checkInDate" required>
		Checkout Date: <input type="text" id="checkOutDate" name="checkOutDate" required>
		<br />
		
		City Located in: 
		<select id="cityName" name="city">
           <zc:forEach var="loc" items="${htlName.htlName}" varStatus="i">
           <option value="${loc}"> ${loc} </option>
           </zc:forEach>
        </select>
		No. of Rooms: 
		<select id="numRooms" name="numRooms">
		   
		   <zc:forEach var="i" begin="1" end="15">
		      <option value="${i}"> ${i} </option>
		   </zc:forEach>
		</select>
		<br />
		Price of room per day
		<input type="text" name="maxPrice">
		<br />
        <input type="hidden" name="action" value="search"/>
        <br>
		<input type="submit" value="Search" style="height:30px;width:180px;font-size:85%"/>
	</form>
</body>
</html>

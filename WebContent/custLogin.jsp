<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="zc" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="roomType" class="comp9321.bean.CustRoomTypeDTO" scope="session" />
<jsp:useBean id="bvf" class="comp9321.bean.BookDTO" scope="session" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Check your Booking ${bvf.bkid}</title>

    
</head>
<body>
<div align="center">
<h1>Customer Booking Management </h1> 
</div>
<div style="color:blue">
<h4>
Booking id = ${bvf.bkid }<br>
Hotel location = ${bvf.city}<br>
Check In date = ${bvf.checkin}<br>
Check Out Date = ${bvf.checkout}<br>

</h4>
</div>
<table align="center" border="2">
    <tr>
        <th> Type of Room </th>
        <th> Extra Bed Added? </th>
        <th> Total Amount Paid </th>
    </tr>
	<zc:forEach var="b" items="${bvf.bookItemList}">
	   <tr>
	       <td>${b.type}</td>
	       <td>${b.xtraBed}</td>
	       <td>${b.amountPaid}</td>
	   </tr>
	</zc:forEach>
</table>
<div  style="color:red">
<H3>Total Amount to be Paid: ${bvf.totalPrice}</H3> 
</div>
<div  style="color:red">
        ${errors}
<zc:choose>
    <zc:when test="${abilityToDropBooking eq 1}">
        <form name="custLogin" action="custLogin" method="POST">
            Would you like to drop the booking?
            <input type="hidden" name="action" value="dropBooking">
            <input type="submit" value="Drop Booking">
        </form>
    </zc:when>
    <zc:otherwise></zc:otherwise>
</zc:choose>
</div>

<br><br>
<h2>Add Rooms to Booking</h2>
<form name="custLogin" action="custLogin" method="POST">
<input type="hidden" name="action" value="addToBooking">

<zc:choose>
    <zc:when test="${confBooking eq 0}">
		Number of rooms:
			<select id="numRooms" name="numRooms">
			   
			   <zc:forEach var="i" begin="1" end="10">
			      <option value="${i }"> ${i } </option>
			   </zc:forEach>
			</select>
		Number of Extra Beds:
			<select id="extraBeds" name="extraBeds">
			   <option value="0">0</option>
			   
			   <zc:forEach var="i" begin="1" end="10">
			      <option value="${i }"> ${i } </option>
			   </zc:forEach>
			</select>
		Add room of type:
		    <select id="roomType" name="roomType">
		       <zc:forEach var="type" items="${roomType.roomType}" varStatus="i">
		       <option value="${i.count}"> ${type} </option>
		       </zc:forEach>
		    </select>
		<input type="submit" name="button" value="Add Room to booking"> 
		<div style="color:blue">
        <a href="http://localhost:8080/Assign2Merge/">Return to Customer Bookings</a>
        </div>   
    </zc:when>
    <zc:otherwise>
        <div style="color:green"><h3>${confMsg}</h3></div>
		<input type="submit" name="button" value="Confirm Room addition">
        <input type="submit" name="button" value="Cancel Room addition">
        <div style="color:blue">
        <a href="http://localhost:8080/Assign2Merge/">Return to Customer Bookings</a>
        </div>	
    </zc:otherwise>
</zc:choose>
</form>


</body>
</html>
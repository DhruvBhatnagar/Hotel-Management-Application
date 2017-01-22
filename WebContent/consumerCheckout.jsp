<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="zcx" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Consumer Checkout</title>

	
	
</head>
<body>
<H1>Booking Checkout</H1>
<table align="center" border="2">
	<tr>
		<th> Room Type </th>
		<th> No. Of Rooms </th>
		<th> No. of Extra Beds </th>
		<th> Total Price over Stay </th>
	</tr>
	<zcx:forEach var="bookItems" items="${CarryDTO}">
	<tr>
		<td>${bookItems.getType() }</td>
		<td>${bookItems.getBedsNum() }</td>
		<td>${bookItems.getXtra_bed()}</td>
    	<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${bookItems.bookPrice}" /></td>
	</tr>
	</zcx:forEach>
</table>
<H4>Total Amount to be Paid: ${overallAmount}</H4> <br>
<form action="ControlServlet" method="post">


<p>
Credit Card Address: <input type="text" name="creditcardinfo" pattern="[0-9]{16}"/>
Email ID: <input type="email" name="emailID" />
<input type="checkbox" name="emailConf" value="1" /> Receive Email Confirmation
</p>
<input type="hidden" name="action" value="checkout" />
<input type="hidden" name="tp" value="${overallAmount}"/>
<p><input type="submit" value="Confirm Booking" style="height:25px;width:180px;font-size:85%"/>
</p>
</form>
<form action="ControlServlet" method="post">
    <input type="hidden" name="action" value="backToSearch" />
    <input type="submit" value="Back to Search" style="height:25px;width:180px;font-size:85%"/>    
</form>
<div  style="color:blue">
		${errors }
</div>
</body>
</html>